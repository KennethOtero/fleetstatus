package com.fleet.status.dao.impl;

import com.fleet.status.dao.repository.AircraftRepository;
import com.fleet.status.dao.IAircraftDAO;
import com.fleet.status.entity.Aircraft;
import jakarta.persistence.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@Profile("dev")
@Slf4j
@RequiredArgsConstructor
public class AircraftDAO implements IAircraftDAO {

    @Autowired
    private AircraftRepository aircraftRepository;

    @Autowired
    private EntityManager entityManager;

    public void save(Aircraft aircraft) throws Exception {
        try {
            aircraftRepository.save(aircraft);
            log.info("Saving new aircraft with tail number {}.", aircraft.getTailNumber());
        } catch (DataIntegrityViolationException e) {
            log.error("Failed to save aircraft. Tail number {} already exists", aircraft.getTailNumber());
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
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


    @Override
    public List<Aircraft> getAllAircraftFromCarrier(int carrierId) {
        try {
            String query = "EXEC uspShowCarrierAircraft @intCarrierId = " + carrierId;
            Query allAircraft = entityManager.createNativeQuery(query, Aircraft.class);
            return allAircraft.getResultList();
        } catch (Exception e) {
            log.error("An error occurred while selecting aircraft from carrier with ID {}: ", carrierId, e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteAircraft(int aircraftId) {
        try {
            // Using StoredProcedureQuery as this stored procedure does not return anything
            StoredProcedureQuery query =  entityManager.createStoredProcedureQuery("uspDeleteAircraft");
            query.registerStoredProcedureParameter("intAircraftId", Integer.class, ParameterMode.IN);
            query.setParameter("intAircraftId", aircraftId);
            query.execute();
            log.info("Deleted aircraft with ID {}", aircraftId);
        } catch (Exception e) {
            log.error("An error occurred while deleting aircraft: ", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Aircraft> findAll() {
        // Returns everything within the TAircraft table
        return (List<Aircraft>) aircraftRepository.findAll();
    }

}
