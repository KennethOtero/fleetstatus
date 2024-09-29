package com.fleet.status.dao;

import com.fleet.status.dto.Carrier;

public interface ICarrierDAO {
    void save(Carrier carrier) throws Exception;

    Carrier findById(int id);
}
