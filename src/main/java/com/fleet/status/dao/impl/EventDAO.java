package com.fleet.status.dao.impl;

import com.fleet.status.dao.IEventDAO;
import com.fleet.status.dao.repository.EventRepository;
import com.fleet.status.dto.Aircraft;
import com.fleet.status.dto.Carrier;
import com.fleet.status.dto.Event;
import com.fleet.status.dto.Type;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.persistence.Tuple;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
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
            List<Tuple> aircraftList = allAircraft.getResultList();
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
            List<Tuple> aircraftList = allAircraft.getResultList();
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
            List<Tuple> aircraftList = allAircraft.getResultList();
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
            Query allAircraft = entityManager.createNativeQuery(query, Aircraft.class);
            List<Tuple> aircraftList = allAircraft.getResultList();
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
            Query allAircraft = entityManager.createNativeQuery(query, Aircraft.class);
            List<Tuple> aircraftList = allAircraft.getResultList();
            return setEventList(aircraftList);
        } catch (Exception e) {
            log.error("An error occurred while selecting in service aircraft from carrier with ID {}: ", carrierId, e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public Event updateAircraftServiceStatus(int aircraftId, int backInService) {
        try {
            String query = "EXEC uspUpdateAircraftServiceStatus @intAircraftId = " + aircraftId + ", @blnBackInService = " + backInService;
            Query allAircraft = entityManager.createNativeQuery(query, Aircraft.class);
            return (Event) allAircraft.getSingleResult();
        } catch (Exception e) {
            log.error("An error occurred while updating aircraft: ", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updateAircraft(Event event) {
        try {
            entityManager.merge(event);
            log.info("Updated aircraft with ID {}", event.getAircraft().getAircraftId());
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
    private List<Event> setEventList(List<Tuple> queryResults) {
        List<Event> events = new ArrayList<>();

        for (Tuple row : queryResults) {
            // Print column names
//            List<TupleElement<?>> elements = row.getElements();
//            for (TupleElement<?> element : elements) {
//                System.out.println("Column: " + element.getAlias());
//            }

            Carrier carrier = new Carrier();
            carrier.setCarrierId((Long) row.get("intCarrierId"));
            carrier.setCarrierName(row.get("strCarrier").toString());

            Type type = new Type();
            type.setTypeId((Long) row.get("intTypeId"));
            type.setTypeName(row.get("strType").toString());

            Aircraft aircraft = new Aircraft();
            aircraft.setAircraftId((Long) row.get("intAircraftId"));
            aircraft.setTailNumber(row.get("strTailNumber").toString());
            aircraft.setType(type);
            aircraft.setCarrier(carrier);

            Event event = new Event();
            event.setAircraft(aircraft);
            event.setRemark(row.get("strRemark").toString());
            event.setReasonString(getReasons(event.getAircraft().getAircraftId()));
            event.setDowntime(row.get("strDowntime").toString());
            event.setBackInService((Integer) row.get("blnBackInService"));
            event.setNextUpdate(row.get("strNextUpdate").toString());
            event.setStartTime(row.get("strStartTime").toString());
            event.setEndTime(row.get("strEndTime").toString());

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
