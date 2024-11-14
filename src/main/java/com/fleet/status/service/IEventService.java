package com.fleet.status.service;

import com.fleet.status.dto.Event;

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

    void updateAircraft(Event event);

    long calculateDownTime(String startTime, String endTime);

    void showBackInService(int aircraftId);

    List<Event> getFilteredEvents(Integer carrierId, Integer typeId, String tailNumber, List<Integer> reasonIds);
}
