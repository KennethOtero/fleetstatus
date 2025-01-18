package com.fleet.status.dao;

import com.fleet.status.entity.Event;

import java.util.List;

public interface IEventDAO {
    void save(Event event) throws Exception;

    Event findById(int id);

    List<Event> findAll();

    List<Event> getHomepageAircraft();

    List<Event> getOutOfServiceAircraft();

    List<Event> getInServiceAircraft();

    List<Event> getAllAircraftFromCarrierOOS(int carrierId);

    List<Event> getAllAircraftFromCarrierIS(int carrierId);

    List<Event> getFilteredEvents(Integer carrierId, Integer typeId, String tailNumber, List<Integer> reasonIds);

    void updateEvent(Event event);
}
