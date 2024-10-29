package com.fleet.status.dao.impl;

import com.fleet.status.dao.IEventDAO;
import com.fleet.status.dao.repository.EventRepository;
import com.fleet.status.dto.Aircraft;
import com.fleet.status.dto.Carrier;
import com.fleet.status.dto.Event;
import com.fleet.status.dto.Type;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Repository
@Profile("dev")
@Slf4j
@RequiredArgsConstructor
public class EventDAO implements IEventDAO {

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private EntityManager entityManager;

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
    public void updateAircraft(Event event) {
        try {
            entityManager.merge(event);
            log.info("Updated event ID {} with aircraft ID {}", event.getEventId() ,event.getAircraft().getAircraftId());
        } catch (Exception e) {
            log.error("An error occurred while updating aircraft: ", e);
            throw new RuntimeException(e);
        }
    }

    private List<Object[]> getReasonsForAircraft(Long aircraftId) {
        try {
            // Call a stored procedure to get the reason information
            Query query = entityManager.createNativeQuery("EXEC uspGetReasonsForAircraft :aircraftId");
            query.setParameter("aircraftId", aircraftId);

            return query.getResultList();
        } catch (Exception e) {
            log.error("An error occurred while fetching reasons for aircraft: ", e);
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
            dtmEndTime,
            strDownTime

       NOTE: Matches order of output from SQL query.
        */

        for (Object[] row : queryResults) {
            List<Object> currentRow = Arrays.stream(row).toList();

            // Exit if query results do not have all 13 columns
            if (currentRow.size() != 13) {
                return null;
            }

            // IDs are automatically converted from Object to Integer.
            // Parse IDs by converting Integer to String to Long.

            Carrier carrier = new Carrier();
            carrier.setCarrierId(Long.parseLong(String.valueOf(currentRow.get(0))));
            carrier.setCarrierName((String) currentRow.get(1));

            Type type = new Type();
            type.setTypeId(Long.parseLong(String.valueOf(currentRow.get(2))));
            type.setTypeName(currentRow.get(3).toString());

            Aircraft aircraft = new Aircraft();
            aircraft.setAircraftId(Long.parseLong(String.valueOf(currentRow.get(4))));
            aircraft.setTailNumber(currentRow.get(5).toString());
            aircraft.setType(type);
            aircraft.setCarrier(carrier);

            Event event = new Event();
            event.setAircraft(aircraft);

            event.setEventId(Long.parseLong(String.valueOf(currentRow.get(6))));
            event.setRemark(currentRow.get(7).toString());
            event.setNextUpdate(currentRow.get(8).toString());
            event.setBackInService((Integer) currentRow.get(9));
            event.setStartTime(currentRow.get(10).toString());
            event.setEndTime(currentRow.get(11).toString());
            event.setDowntime(currentRow.get(12).toString());

            // Set reasons
            event.setReasonString(getReasons(event.getAircraft().getAircraftId()));

            // Add event to list
            events.add(event);
        }

        return events;
    }

    /**
     * Formats reasons into a string
     * @param aircraftId aircraft
     * @return comma separated reasons
     */
    private String getReasons(Long aircraftId) {
        List<Object[]> reasons = getReasonsForAircraft(aircraftId);

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
