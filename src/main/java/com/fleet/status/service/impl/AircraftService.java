package com.fleet.status.service.impl;

import com.fleet.status.dao.impl.AircraftDAO;
import com.fleet.status.dto.Aircraft;
import com.fleet.status.service.IAircraftService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@Profile("dev")
public class AircraftService implements IAircraftService {

    @Autowired
    private AircraftDAO aircraftDAO;

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
        return aircraftDAO.getAllAircraftFromCarrier(carrierId);
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
