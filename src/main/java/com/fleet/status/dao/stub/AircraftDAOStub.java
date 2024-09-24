package com.fleet.status.dao.stub;

import com.fleet.status.dao.IAircraftDAO;
import com.fleet.status.dto.Aircraft;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
@Profile("test")
public class AircraftDAOStub implements IAircraftDAO {

    // Store data locally for CircleCI
    Map<Integer, Aircraft> aircraftRepository = new HashMap<>();

    @Override
    public boolean save(Aircraft aircraft) throws Exception {
        aircraftRepository.put(aircraft.getAircraftId(), aircraft);
        return false;
    }
}
