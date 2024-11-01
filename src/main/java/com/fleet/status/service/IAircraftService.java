package com.fleet.status.service;

import com.fleet.status.dto.Aircraft;

import java.util.List;

public interface IAircraftService {

    Aircraft findById(int id);

    void save(Aircraft aircraft) throws Exception;

    List<Aircraft> getAllAircraftFromCarrier(int carrierId);

    void deleteAircraft(int aircraftId);

    List<Aircraft> findAll();
}
