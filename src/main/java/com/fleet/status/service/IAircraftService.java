package com.fleet.status.service;

import com.fleet.status.dto.Aircraft;

import java.util.List;

public interface IAircraftService {

    Aircraft fetchById(int id);

    void save(Aircraft aircraft);

    List<Aircraft> getOutofServiceAircraft();
}
