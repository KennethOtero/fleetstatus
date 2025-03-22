package com.fleet.status.dao;

import com.fleet.status.dao.repository.EventRepository;
import com.fleet.status.dao.repository.ReasonRepository;
import com.fleet.status.entity.Aircraft;
import com.fleet.status.entity.Carrier;
import com.fleet.status.entity.Event;
import com.fleet.status.entity.Type;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Repository
@Slf4j
@RequiredArgsConstructor
public class EventDAO {

    private final EventRepository eventRepository;
    private final ReasonRepository reasonRepository;
    private final EntityManager entityManager;

    public void save(Event event) {
        eventRepository.save(event);
    }

    public Event findById(int id) {
        return eventRepository.findById(id).orElse(null);
    }

    public List<Event> getHomepageEvents() {
        return eventRepository.getHomepageAircraft();
    }

    public List<Event> getOutOfServiceEvents() {
        return eventRepository.getOutOfServiceAircraft();
    }

    public List<Event> getFilteredEvents(Integer carrierId, Integer typeId, String tailNumber, List<Integer> reasonIds, LocalDateTime startDate, LocalDateTime endDate) {
        try {
            StringBuilder queryBuilder = new StringBuilder("SELECT * FROM vEventHistory WHERE 1=1");

            if (carrierId != null) {
                queryBuilder.append(" AND intCarrierId = :carrierId");
            }
            if (typeId != null) {
                queryBuilder.append(" AND intTypeId = :typeId");
            }
            if (tailNumber != null && !tailNumber.isEmpty()) {
                queryBuilder.append(" AND strTailNumber LIKE :tailNumber");
            }
            if (startDate != null && endDate != null) {
                queryBuilder.append(" AND (")
                        .append("(dtmStartTime BETWEEN :startDate AND :endDate) OR ") // The shutdown start time is within the range
                        .append("(dtmEndTime BETWEEN :startDate AND :endDate) OR ")   // The shutdown end time is within the range
                        .append("(dtmStartTime <= :startDate AND dtmEndTime >= :endDate)") // Downtime covers the entire range
                        .append(")");
            }

            // By using GROUP BY and HAVING in the sub-query, only those event ID records are kept whose number of intReasonId is equal to the number of reasons in the reasonIds list.
            if (reasonIds != null && !reasonIds.isEmpty()) {
                queryBuilder.append(" AND intEventId IN (")
                        .append("SELECT intEventId FROM TEventReasons ")
                        .append("WHERE intReasonId IN :reasonIds ")
                        .append("GROUP BY intEventId ")
                        .append("HAVING COUNT(DISTINCT intReasonId) = :reasonCount)");
            }

            Query query = entityManager.createNativeQuery(queryBuilder.toString());

            if (carrierId != null) {
                query.setParameter("carrierId", carrierId);
            }
            if (typeId != null) {
                query.setParameter("typeId", typeId);
            }
            if (tailNumber != null && !tailNumber.isEmpty()) {
                query.setParameter("tailNumber", "%" + tailNumber + "%");
            }
            if (reasonIds != null && !reasonIds.isEmpty()) {
                query.setParameter("reasonIds", reasonIds);
                query.setParameter("reasonCount", reasonIds.size()); // Pass the reason number for comparison
            }
            if (startDate != null) {
                query.setParameter("startDate", startDate);
            }
            if (endDate != null) {
                query.setParameter("endDate", endDate);
            }

            List<Object[]> results = query.getResultList();
            return mapEvents(results);
        } catch (Exception e) {
            log.error("An error occurred while selecting all event history ", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * Creates a list of Event objects from the query results
     * @param queryResults result
     * @return list of Event objects
     */
    public List<Event> mapEvents(List<Object[]> queryResults) {
        List<Event> events = new ArrayList<>();

        /*
        Maps results to an Event DTO from the following 13 columns:
            intCarrierId,
            strCarrier,
            intTypeId,
            strType,
            intAircraftId,
            strTailNumber,
            intEventId,
            strRemark,
            dtmNextUpdate,
            blnBackInService,
            dtmStartTime,
            dtmEndTime

       NOTE: Matches order of output from SQL query.
        */

        for (Object[] row : queryResults) {
            List<Object> currentRow = Arrays.stream(row).toList();

            // Exit if query results do not have all 12 columns
            if (currentRow.size() != 12) {
                return null;
            }

            Carrier carrier = new Carrier();
            carrier.setCarrierId(Long.valueOf(validateEventFields(currentRow.get(0))));
            carrier.setCarrierName(validateEventFields(currentRow.get(1)));

            Type type = new Type();
            type.setTypeId(Long.valueOf(validateEventFields(currentRow.get(2))));
            type.setTypeName(validateEventFields(currentRow.get(3)));

            Aircraft aircraft = new Aircraft();
            aircraft.setAircraftId(Long.valueOf(validateEventFields(currentRow.get(4))));
            aircraft.setTailNumber(validateEventFields(currentRow.get(5)));
            aircraft.setType(type);
            aircraft.setCarrier(carrier);

            Event event = new Event();
            event.setAircraft(aircraft);

            event.setEventId(Long.valueOf(validateEventFields(currentRow.get(6))));
            event.setRemark(validateEventFields(currentRow.get(7)));
            Timestamp nextUpdate = (Timestamp) currentRow.get(8);
            event.setNextUpdate(nextUpdate.toInstant());
            event.setBackInService(Integer.valueOf(validateEventFields(currentRow.get(9))));
            Timestamp startTime = (Timestamp)currentRow.get(10);
            event.setStartTime(startTime.toInstant());
            Timestamp endTime = (Timestamp)currentRow.get(11);
            event.setEndTime(endTime != null ? endTime.toInstant() : null);

            // Set reasons
            event.setReason(reasonRepository.getReasonsForEvent(event.getEventId()));

            // Add event to list
            events.add(event);
        }

        return events;
    }

    private String validateEventFields(Object item) {
        return (item != null) ? item.toString() : null;
    }
}
