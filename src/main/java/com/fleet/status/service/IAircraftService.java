package com.fleet.status.service;

import com.fleet.status.dto.Aircraft;

import java.util.List;

public interface IAircraftService {

    Aircraft fetchById(int id);

    void save(Aircraft aircraft) throws Exception;

    List<Aircraft> getOutofServiceAircraft();

    List<Aircraft> getAllAircraft();
}
