package com.fleet.status.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fleet.status.dao.impl.EventDAO;
import com.fleet.status.entity.Event;
import com.fleet.status.service.IEventService;
import com.github.fge.jsonpatch.JsonPatch;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@Slf4j
@Profile("dev")
@RequiredArgsConstructor
public class EventService implements IEventService {
    private final EventDAO eventDAO;

    @Override
    public void save(Event event) throws Exception {
        eventDAO.save(event);
    }

    @Override
    public Event findById(int id) {
        return eventDAO.findById(id);
    }

    @Override
    public List<Event> findAll() {
        return eventDAO.findAll();
    }

    @Override
    public List<Event> getHomepageAircraft() {
        return eventDAO.getHomepageAircraft();
    }

    @Override
    public List<Event> getOutOfServiceAircraft() {
        return eventDAO.getOutOfServiceAircraft();
    }

    @Override
    public List<Event> getInServiceAircraft() {
        return eventDAO.getInServiceAircraft();
    }

    @Override
    public List<Event> getAllAircraftFromCarrierOOS(int carrierId) {
        return eventDAO.getAllAircraftFromCarrierOOS(carrierId);
    }

    @Override
    public List<Event> getAllAircraftFromCarrierIS(int carrierId) {
        return eventDAO.getAllAircraftFromCarrierIS(carrierId);
    }

    @Override
    public void updateEvent(Event event) {
        eventDAO.updateEvent(event);
    }

    @Override
    public long calculateDownTime(String startTime, String endTime) {
        // SQL may return decimal point after seconds. Remove if needed.
        if (startTime.contains(".")) {
            int period = startTime.indexOf(".");
            startTime = startTime.substring(0, period);
        }

        if (endTime.contains(".")) {
            int period = endTime.indexOf(".");
            endTime = endTime.substring(0, period);
        }

        String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

        LocalDateTime start = LocalDateTime.parse(startTime, DateTimeFormatter.ofPattern(DATE_FORMAT));
        LocalDateTime end = LocalDateTime.parse(endTime, DateTimeFormatter.ofPattern(DATE_FORMAT));

        Duration duration = Duration.between(start, end);

        return Math.abs(duration.toHours());
    }

    @Override
    public void showBackInService(int eventId) {
        Event event = eventDAO.findById(eventId);
        Instant now = Instant.now();
        event.setEndTime(now);
        event.setBackInService(1);
        eventDAO.updateEvent(event);
    }

    /**
     * Converts the fields to update within the JsonPatch object to an Event object
     * @param patch - new fields to update
     * @param event - original Event
     * @return - Updated Event object
     */
    @Override
    public Event patchEvent(JsonPatch patch, Event event) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule()); // Needed to handle dates
            JsonNode patched = patch.apply(mapper.convertValue(event, JsonNode.class));
            return mapper.treeToValue(patched, Event.class);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Event> getFilteredEvents(Integer carrierId, Integer typeId, String tailNumber, List<Integer> reasonIds) {
        return eventDAO.getFilteredEvents(carrierId, typeId, tailNumber, reasonIds);
    }
}
