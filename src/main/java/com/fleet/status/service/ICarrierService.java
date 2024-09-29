package com.fleet.status.service;

import com.fleet.status.dto.Carrier;

public interface ICarrierService {
    void save (Carrier carrier) throws Exception;

    Carrier findById(int id);
}
