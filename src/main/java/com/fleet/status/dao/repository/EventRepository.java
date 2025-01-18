package com.fleet.status.dao.repository;

import com.fleet.status.entity.Event;
import org.springframework.context.annotation.Profile;
import org.springframework.data.repository.CrudRepository;

@Profile("!test")
public interface EventRepository extends CrudRepository<Event, Integer> {
}
