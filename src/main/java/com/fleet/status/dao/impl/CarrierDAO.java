package com.fleet.status.dao.impl;

import com.fleet.status.dao.repository.CarrierRepository;
import com.fleet.status.dao.ICarrierDAO;
import com.fleet.status.entity.Carrier;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Slf4j
@Repository
@Profile("dev")
@RequiredArgsConstructor
public class CarrierDAO implements ICarrierDAO {

    private final CarrierRepository carrierRepository;
    private final EntityManager entityManager;

    @Override
    public void save(Carrier carrier) throws Exception {
        carrierRepository.save(carrier);
    }

    @Override
    public Carrier findById(int id) {
        try {
            Optional<Carrier> entityOptional = carrierRepository.findById(id);
            if (entityOptional.isPresent()) {
                return entityOptional.get();
            } else {
                log.info("Failed to find carrier with ID: {}. Returning null.", id);
                return null;
            }
        } catch (Exception e) {
            log.error("An error occurred fetching carrier with ID {}. Error: {}", id, e.getMessage());
            return null;
        }
    }

    @Override
    public List<Carrier> getAllCarrier() {
        try {
            String query = "SELECT * FROM vAllCarrier";
            Query allReason = entityManager.createNativeQuery(query, Carrier.class);
            return allReason.getResultList();
        } catch (Exception e) {
            log.error("An error occurred while selecting all aircraft: ", e);
            throw new RuntimeException(e);
        }
    }
}
