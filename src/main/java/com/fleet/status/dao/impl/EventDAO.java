package com.fleet.status.dao.impl;

import com.fleet.status.dao.IEventDAO;
import com.fleet.status.dao.repository.EventRepository;
import com.fleet.status.dto.Event;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Profile("dev")
@Slf4j
@RequiredArgsConstructor
public class EventDAO implements IEventDAO {

    @Autowired
    private EventRepository eventRepository;

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
}
