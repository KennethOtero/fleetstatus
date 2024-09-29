package com.fleet.status.service.impl;

import com.fleet.status.dao.ICarrierDAO;
import com.fleet.status.dto.Carrier;
import com.fleet.status.service.ICarrierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile("dev")
public class CarrierService implements ICarrierService {

    @Autowired
    private ICarrierDAO carrierDAO;

    @Override
    public void save(Carrier carrier) throws Exception {
        carrierDAO.save(carrier);
    }

    @Override
    public Carrier findById(int id) {
        return carrierDAO.findById(id);
    }
}
