package com.fleet.status.dao.impl;

import com.fleet.status.dao.repository.AircraftRepository;
import com.fleet.status.dao.IAircraftDAO;
import com.fleet.status.dto.Aircraft;
import jakarta.persistence.EntityManager;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.Query;
import jakarta.persistence.StoredProcedureQuery;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
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

    @Override
    public List<Aircraft> getAllAircraft() {
        try {
            String query = "SELECT * FROM vAllAircraft";
            Query allAircraft = entityManager.createNativeQuery(query, Aircraft.class);
            return allAircraft.getResultList();
        } catch (Exception e) {
            log.error("An error occurred while selecting all aircraft: ", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Aircraft> getOutOfServiceAircraft() {
        try {
            String query = "SELECT * FROM vOutOfServiceAircraft";
            Query allAircraft = entityManager.createNativeQuery(query, Aircraft.class);
            return allAircraft.getResultList();
        } catch (Exception e) {
            log.error("An error occurred while selecting all out of service aircraft: ", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Aircraft> getInServiceAircraft() {
        try {
            String query = "SELECT * FROM vInServiceAircraft";
            Query allAircraft = entityManager.createNativeQuery(query, Aircraft.class);
            return allAircraft.getResultList();
        } catch (Exception e) {
            log.error("An error occurred while selecting all in service aircraft: ", e);
            throw new RuntimeException(e);
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
    public List<Aircraft> getAllAircraftFromCarrierOOS(int carrierId) {
        try {
            String query = "EXEC uspShowCarrierAircraftOOS @intCarrierId = " + carrierId;
            Query allAircraft = entityManager.createNativeQuery(query, Aircraft.class);
            return allAircraft.getResultList();
        } catch (Exception e) {
            log.error("An error occurred while selecting out of service aircraft from carrier with ID {}: ", carrierId, e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Aircraft> getAllAircraftFromCarrierIS(int carrierId) {
        try {
            String query = "EXEC uspShowCarrierAircraftIS @intCarrierId = " + carrierId;
            Query allAircraft = entityManager.createNativeQuery(query, Aircraft.class);
            return allAircraft.getResultList();
        } catch (Exception e) {
            log.error("An error occurred while selecting in service aircraft from carrier with ID {}: ", carrierId, e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public Aircraft updateAircraftServiceStatus(int aircraftId, int backInService) {
        try {
            String query = "EXEC uspUpdateAircraftServiceStatus @intAircraftId = " + aircraftId + ", @blnBackInService = " + backInService;
            Query allAircraft = entityManager.createNativeQuery(query, Aircraft.class);
            return (Aircraft) allAircraft.getSingleResult();
        } catch (Exception e) {
            log.error("An error occurred while updating aircraft: ", e);
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
        } catch (Exception e) {
            log.error("An error occurred while deleting aircraft: ", e);
            throw new RuntimeException(e);
        }
    }

}
