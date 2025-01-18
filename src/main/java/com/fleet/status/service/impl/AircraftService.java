package com.fleet.status.service.impl;

import com.fleet.status.dao.impl.AircraftDAO;
import com.fleet.status.entity.Aircraft;
import com.fleet.status.service.IAircraftService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Profile("dev")
@RequiredArgsConstructor
public class AircraftService implements IAircraftService {

    private final AircraftDAO aircraftDAO;

    @Override
    public Aircraft findById(int id) {
        return aircraftDAO.findById(id);
    }

    @Override
    public void save(Aircraft aircraft) throws Exception {
        aircraftDAO.save(aircraft);
    }

    @Override
    public List<Aircraft> getAllAircraftFromCarrier(int carrierId) {
        return List.of();
    }

    @Override
    public void deleteAircraft(int aircraftId) {
        aircraftDAO.deleteAircraft(aircraftId);
    }

    @Override
    public List<Aircraft> findAll() {
        return aircraftDAO.findAll();
    }
}
