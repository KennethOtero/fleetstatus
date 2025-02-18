package com.fleet.status.service;

import com.fleet.status.dao.repository.AircraftRepository;
import com.fleet.status.entity.Aircraft;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Profile("dev")
@RequiredArgsConstructor
public class AircraftService {

    private final AircraftRepository aircraftRepository;

    public void save(Aircraft aircraft) {
        aircraftRepository.save(aircraft);
    }

    @Transactional
    public void deleteAircraft(int aircraftId) {
        aircraftRepository.deleteAircraft(aircraftId);
    }

    public List<Aircraft> findAll() {
        return (List<Aircraft>) aircraftRepository.findAll();
    }
}
