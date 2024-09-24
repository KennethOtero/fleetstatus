package com.fleet.status.dao;

import com.fleet.status.dto.Aircraft;
import org.springframework.context.annotation.Profile;
import org.springframework.data.repository.CrudRepository;

@Profile("!test")
public interface AircraftRepository extends CrudRepository<Aircraft, Integer> {
}
