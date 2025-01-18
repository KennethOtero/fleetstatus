package com.fleet.status.dao.impl;

import com.fleet.status.dao.IReasonDAO;
import com.fleet.status.dao.repository.ReasonRepository;
import com.fleet.status.entity.Reason;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.List;

@Slf4j
@Repository
@Profile("dev")
@RequiredArgsConstructor
public class ReasonDAO implements IReasonDAO {

    private final ReasonRepository reasonRepository;
    private final EntityManager entityManager;


    @Override
    public void save(Reason reason) throws Exception {

        reasonRepository.save(reason);
    }

    @Override
    public Reason findById(int id) {
        return null;
    }

    @Override
    public List<Reason> getAllReason() {
        try {
            String query = "SELECT * FROM vAllReason";
            Query allReason = entityManager.createNativeQuery(query, Reason.class);
            return allReason.getResultList();
        } catch (Exception e) {
            log.error("An error occurred while selecting all aircraft: ", e);
            throw new RuntimeException(e);
        }
    }
}
