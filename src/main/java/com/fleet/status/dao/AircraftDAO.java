package com.fleet.status.dao;

import com.fleet.status.dto.Aircraft;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository("AircraftDAO")
public class AircraftDAO {

    @Autowired
    private FleetStatusRepository fleetStatusRepository;

    public boolean save(Aircraft aircraft) throws Exception {
        fleetStatusRepository.save(aircraft);
        return false;
    }

}
