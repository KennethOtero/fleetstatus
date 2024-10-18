package com.fleet.status.service;

import com.fleet.status.dto.Carrier;

import java.util.List;

public interface ICarrierService {
    void save (Carrier carrier) throws Exception;

    Carrier findById(int id);

    List<Carrier> getAllCarrier();
}
