package com.fleet.status.service.impl;

import com.fleet.status.dao.impl.AircraftDAO;
import com.fleet.status.dto.Aircraft;
import com.fleet.status.service.IAircraftService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Profile("dev")
public class AircraftService implements IAircraftService {

    @Autowired
    private AircraftDAO aircraftDAO;

    @Override
    public Aircraft fetchById(int id) {
        return aircraftDAO.findById(id);
    }

    @Override
    public void save(Aircraft aircraft) throws Exception {
        aircraftDAO.save(aircraft);
    }

    @Override
    public List<Aircraft> getOutofServiceAircraft() {
        return aircraftDAO.getOutOfServiceAircraft();
    }

    @Override
    public List<Aircraft> getAllAircraft() {
        return aircraftDAO.getAllAircraft();
    }

    @Override
    public List<Aircraft> getOutOfServiceAircraft() {
        return aircraftDAO.getOutOfServiceAircraft();
    }

    @Override
    public List<Aircraft> getInServiceAircraft() {
        return aircraftDAO.getInServiceAircraft();
    }

    @Override
    public List<Aircraft> getAllAircraftFromCarrier(int carrierId) {
        return aircraftDAO.getAllAircraftFromCarrier(carrierId);
    }

    @Override
    public List<Aircraft> getAllAircraftFromCarrierOOS(int carrierId) {
        return aircraftDAO.getAllAircraftFromCarrierOOS(carrierId);
    }

    @Override
    public List<Aircraft> getAllAircraftFromCarrierIS(int carrierId) {
        return aircraftDAO.getAllAircraftFromCarrierIS(carrierId);
    }

    @Override
    public Aircraft updateAircraftServiceStatus(int aircraftId, int backInService) {
        return aircraftDAO.updateAircraftServiceStatus(aircraftId, backInService);
    }

    @Override
    public void deleteAircraft(int aircraftId) {
        aircraftDAO.deleteAircraft(aircraftId);
    }
}
