package com.fleet.status.dao.stub;

import com.fleet.status.dao.IAircraftDAO;
import com.fleet.status.dto.Aircraft;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@Profile("test")
public class AircraftDAOStub implements IAircraftDAO {

    // Store data locally for CircleCI
    Map<Long, Aircraft> aircraftRepository = new HashMap<>();

    @Override
    public void save(Aircraft aircraft) {
        aircraftRepository.put(aircraft.getAircraftId(), aircraft);
    }

    @Override
    public Aircraft findById(int id) {
        return aircraftRepository.get((long) id);
    }

    @Override
    public List<Aircraft> getAllAircraft() {
        return List.of();
    }

    @Override
    public List<Aircraft> getOutOfServiceAircraft() {
        return List.of();
    }

    @Override
    public List<Aircraft> getInServiceAircraft() {
        return List.of();
    }
}
