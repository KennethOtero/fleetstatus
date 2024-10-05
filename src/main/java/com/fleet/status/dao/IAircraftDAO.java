package com.fleet.status.dao;

import com.fleet.status.dto.Aircraft;

import java.util.List;

public interface IAircraftDAO {
    void save(Aircraft aircraft) throws Exception;

    Aircraft findById(int id);

    List<Aircraft> getAllAircraft();

    List<Aircraft> getOutOfServiceAircraft();

    List<Aircraft> getInServiceAircraft();
}
