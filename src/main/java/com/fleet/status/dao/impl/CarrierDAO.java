package com.fleet.status.dao.impl;

import com.fleet.status.dao.repository.CarrierRepository;
import com.fleet.status.dao.ICarrierDAO;
import com.fleet.status.dto.Carrier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

@Repository
@Profile("dev")
public class CarrierDAO implements ICarrierDAO {

    @Autowired
    private CarrierRepository carrierRepository;

    @Override
    public void save(Carrier carrier) {
        carrierRepository.save(carrier);
    }
}
