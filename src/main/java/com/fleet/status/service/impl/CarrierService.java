package com.fleet.status.service.impl;

import com.fleet.status.dao.ICarrierDAO;
import com.fleet.status.entity.Carrier;
import com.fleet.status.service.ICarrierService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Profile("dev")
@RequiredArgsConstructor
public class CarrierService implements ICarrierService {

    private final ICarrierDAO carrierDAO;

    @Override
    public void save(Carrier carrier) throws Exception {
        carrierDAO.save(carrier);
    }

    @Override
    public Carrier findById(int id) {
        return carrierDAO.findById(id);
    }

    @Override
    public List<Carrier> getAllCarrier() {
        return carrierDAO.getAllCarrier();
    }
}
