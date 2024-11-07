package com.fleet.status.service.impl;

import com.fleet.status.dao.impl.EventDAO;
import com.fleet.status.dto.Event;
import com.fleet.status.service.IEventService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@Slf4j
@Profile("dev")
public class EventService implements IEventService {
    @Autowired
    private EventDAO eventDAO;

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
    public void updateAircraft(Event event) {
        eventDAO.updateAircraft(event);
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
        eventDAO.updateAircraft(event);
    }
}
