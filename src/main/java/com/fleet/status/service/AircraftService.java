package com.fleet.status.service;

import com.fleet.status.dao.repository.AircraftRepository;
import com.fleet.status.entity.Aircraft;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Profile("!test")
@RequiredArgsConstructor
@Slf4j
public class AircraftService {

    private final AircraftRepository aircraftRepository;

    public void save(Aircraft aircraft) {
        Aircraft savedAircraft = aircraftRepository.save(aircraft);
        log.info("Saved Aircraft with Id: {}", savedAircraft.getAircraftId());
    }

    @Transactional
    public void deleteAircraft(int aircraftId) {
        aircraftRepository.deleteAircraft(aircraftId);
        log.info("Deleted Aircraft with Id: {}", aircraftId);
    }

    public List<Aircraft> findAll() {
        return (List<Aircraft>) aircraftRepository.findAll();
    }
}
