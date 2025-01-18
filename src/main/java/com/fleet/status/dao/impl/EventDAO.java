package com.fleet.status.dao.impl;

import com.fleet.status.dao.IEventDAO;
import com.fleet.status.dao.repository.EventRepository;
import com.fleet.status.entity.Aircraft;
import com.fleet.status.entity.Carrier;
import com.fleet.status.entity.Event;
import com.fleet.status.entity.Type;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Repository
@Profile("dev")
@Slf4j
@RequiredArgsConstructor
public class EventDAO implements IEventDAO {

    private final EventRepository eventRepository;
    private final EntityManager entityManager;

    @Override
    public void save(Event event) throws Exception {
        eventRepository.save(event);
    }

    @Override
    public Event findById(int id) {
        return eventRepository.findById(id).orElse(null);
    }

    @Override
    public List<Event> findAll() {
        return (List<Event>) eventRepository.findAll();
    }

    @Override
    public List<Event> getHomepageAircraft() {
        try {
            String query = "SELECT * FROM vAllAircraft WHERE [blnBackInService] = 0 " +
                    "OR (dtmEndTime IS NOT NULL AND DATEDIFF(minute, dtmEndTime, GETUTCDATE()) < 30)";
            Query allAircraft = entityManager.createNativeQuery(query);
            List<Object[]> aircraftList = allAircraft.getResultList();
            return setEventList(aircraftList);
        } catch (Exception e) {
            log.error("An error occurred while selecting all aircraft: ", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Event> getOutOfServiceAircraft() {
        try {
            String query = "SELECT * FROM vOutOfServiceAircraft";
            Query allAircraft = entityManager.createNativeQuery(query);
            List<Object[]> aircraftList = allAircraft.getResultList();
            return setEventList(aircraftList);
        } catch (Exception e) {
            log.error("An error occurred while selecting all out of service aircraft: ", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Event> getInServiceAircraft() {
        try {
            String query = "SELECT * FROM vInServiceAircraft";
            Query allAircraft = entityManager.createNativeQuery(query);
            List<Object[]> aircraftList = allAircraft.getResultList();
            return setEventList(aircraftList);
        } catch (Exception e) {
            log.error("An error occurred while selecting all in service aircraft: ", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Event> getAllAircraftFromCarrierOOS(int carrierId) {
        try {
            String query = "EXEC uspShowCarrierAircraftOOS @intCarrierId = " + carrierId;
            Query allAircraft = entityManager.createNativeQuery(query);
            List<Object[]> aircraftList = allAircraft.getResultList();
            return setEventList(aircraftList);
        } catch (Exception e) {
            log.error("An error occurred while selecting out of service aircraft from carrier with ID {}: ", carrierId, e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Event> getAllAircraftFromCarrierIS(int carrierId) {
        try {
            String query = "EXEC uspShowCarrierAircraftIS @intCarrierId = " + carrierId;
            Query allAircraft = entityManager.createNativeQuery(query);
            List<Object[]> aircraftList = allAircraft.getResultList();
            return setEventList(aircraftList);
        } catch (Exception e) {
            log.error("An error occurred while selecting in service aircraft from carrier with ID {}: ", carrierId, e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updateEvent(Event event) {
        try {
            entityManager.merge(event);
            log.info("Updated event ID {} with aircraft ID {}", event.getEventId() ,event.getAircraft().getAircraftId());
        } catch (Exception e) {
            log.error("An error occurred while updating aircraft: ", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Event> getFilteredEvents(Integer carrierId, Integer typeId, String tailNumber, List<Integer> reasonIds) {
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

            List<Object[]> results = query.getResultList();
            return setEventList(results);
        } catch (Exception e) {
            log.error("An error occurred while selecting all event history ", e);
            throw new RuntimeException(e);
        }
    }


    private List<Object[]> getReasonsForEvent(Long eventId) {
        try {
            // Call a stored procedure to get the reason information
            Query query = entityManager.createNativeQuery("EXEC uspGetReasonsForEvent :eventId");
            query.setParameter("eventId", eventId);

            return query.getResultList();
        } catch (Exception e) {
            log.error("An error occurred while fetching reasons for event: ", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * Creates an Event object from the query results
     * @param queryResults result
     * @return list of Event objects
     */
    private List<Event> setEventList(List<Object[]> queryResults) {
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
            event.setReasonString(getReasons(event.getAircraft().getAircraftId()));

            // Add event to list
            events.add(event);
        }

        return events;
    }

    private String validateEventFields(Object item) {
        return (item != null) ? item.toString() : null;
    }

    /**
     * Formats reasons into a string
     * @param eventId eventId
     * @return comma separated reasons
     */
    private String getReasons(Long eventId) {
        List<Object[]> reasons = getReasonsForEvent(eventId);

        // Object[0] stand for intReasonId
        // Object[1] stand for strReason
        if (reasons != null && !reasons.isEmpty()) {
            return reasons.stream()
                    .map(result -> (String) result[1])
                    .collect(Collectors.joining(", "));
        }
        return "";
    }
}
