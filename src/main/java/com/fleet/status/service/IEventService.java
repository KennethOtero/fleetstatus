package com.fleet.status.service;

import com.fleet.status.entity.Event;
import com.github.fge.jsonpatch.JsonPatch;

import java.time.Instant;
import java.util.List;

public interface IEventService {
    void save(Event event) throws Exception;

    Event findById(int id);

    List<Event> findAll();

    List<Event> getHomepageAircraft();

    List<Event> getOutOfServiceAircraft();

    List<Event> getInServiceAircraft();

    List<Event> getAllAircraftFromCarrierOOS(int carrierId);

    List<Event> getAllAircraftFromCarrierIS(int carrierId);

    void updateEvent(Event event);

    long calculateDownTime(String startTime, String endTime);

    void showBackInService(int aircraftId, Instant backInServiceDate);

    Event patchEvent(JsonPatch patch, Event event);

    List<Event> getFilteredEvents(Integer carrierId, Integer typeId, String tailNumber, List<Integer> reasonIds);
}
