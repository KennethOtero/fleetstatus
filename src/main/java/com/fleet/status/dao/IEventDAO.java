package com.fleet.status.dao;

import com.fleet.status.dto.Aircraft;
import com.fleet.status.dto.Event;

import java.util.List;

public interface IEventDAO {
    void save(Event event) throws Exception;

    Event findById(int id);

    List<Event> findAll();
}
