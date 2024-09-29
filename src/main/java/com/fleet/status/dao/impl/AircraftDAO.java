package com.fleet.status.dao.impl;

import com.fleet.status.dao.ICarrierDAO;
import com.fleet.status.dao.repository.AircraftRepository;
import com.fleet.status.dao.IAircraftDAO;
import com.fleet.status.dto.Aircraft;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@Profile("dev")
@Slf4j
public class AircraftDAO implements IAircraftDAO {

    @Autowired
    private AircraftRepository aircraftRepository;

    @Autowired
    private ICarrierDAO carrierDAO;

    public void save(Aircraft aircraft) throws Exception {
        aircraftRepository.save(aircraft);
    }

    @Override
    public Aircraft findById(int id) {
        try {
            Optional<Aircraft> entityOptional = aircraftRepository.findById(id);
            if (entityOptional.isPresent()) {
                return entityOptional.get();
            } else {
                log.info("Aircraft with ID: {} does not exist. Returning null.", id);
                return null;
            }
        } catch (Exception e) {
            log.error("An error occurred while fetching aircraft: ", e);
            return null;
        }
    }

}
