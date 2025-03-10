package com.fleet.status.dao.repository;

import com.fleet.status.entity.Carrier;
import org.springframework.context.annotation.Profile;
import org.springframework.data.repository.CrudRepository;

@Profile("!test")
public interface CarrierRepository extends CrudRepository<Carrier, Integer> {

}
