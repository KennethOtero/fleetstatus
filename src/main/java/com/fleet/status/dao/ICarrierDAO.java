package com.fleet.status.dao;

import com.fleet.status.entity.Carrier;

import java.util.List;

public interface ICarrierDAO {
    void save(Carrier carrier) throws Exception;

    Carrier findById(int id);

    List<Carrier> getAllCarrier();
}
