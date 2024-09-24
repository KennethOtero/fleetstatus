package com.fleet.status.dao;

import com.fleet.status.dto.Aircraft;

public interface IAircraftDAO {
    boolean save(Aircraft aircraft) throws Exception;
}
