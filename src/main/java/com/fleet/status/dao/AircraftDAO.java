package com.fleet.status.dao;

import com.fleet.status.dto.Aircraft;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

@Repository
@Profile("dev")
public class AircraftDAO implements IAircraftDAO {

    @Autowired
    private AircraftRepository aircraftRepository;

    public boolean save(Aircraft aircraft) throws Exception {
        aircraftRepository.save(aircraft);
        return false;
    }

}
