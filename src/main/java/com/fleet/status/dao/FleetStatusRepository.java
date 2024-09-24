package com.fleet.status.dao;

import com.fleet.status.dto.Aircraft;
import org.springframework.data.repository.CrudRepository;

public interface FleetStatusRepository extends CrudRepository<Aircraft, Integer> {
}
