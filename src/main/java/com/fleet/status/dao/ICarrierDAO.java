package com.fleet.status.dao;

import com.fleet.status.dto.Carrier;

import java.util.List;

public interface ICarrierDAO {
    void save(Carrier carrier) throws Exception;

    Carrier findById(int id);

    List<Carrier> getAllCarrier();
}
