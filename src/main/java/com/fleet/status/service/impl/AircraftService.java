package com.fleet.status.service.impl;

import com.fleet.status.dao.repository.AircraftRepository;
import com.fleet.status.entity.Aircraft;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Profile("dev")
@RequiredArgsConstructor
public class AircraftService {

    private final AircraftRepository aircraftRepository;

    public Aircraft findById(int id) {
        Optional<Aircraft> foundAircraft = aircraftRepository.findById(id);
        return foundAircraft.orElse(null);
    }

    public void save(Aircraft aircraft) throws Exception {
        aircraftRepository.save(aircraft);
    }

    public List<Aircraft> getAllAircraftFromCarrier(int carrierId) {
        return aircraftRepository.getAllAircraftFromCarrier(carrierId);
    }

    public void deleteAircraft(int aircraftId) {
        aircraftRepository.deleteAircraft(aircraftId);
    }

    public List<Aircraft> findAll() {
        return (List<Aircraft>) aircraftRepository.findAll();
    }
}
