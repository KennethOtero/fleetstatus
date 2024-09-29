package com.fleet.status.dao;

import com.fleet.status.dto.Aircraft;

public interface IAircraftDAO {
    void save(Aircraft aircraft) throws Exception;

    Aircraft findById(int id);
}
