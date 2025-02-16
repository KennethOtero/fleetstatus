package com.fleet.status.service.impl;

import com.fleet.status.dao.repository.CarrierRepository;
import com.fleet.status.entity.Carrier;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Profile("dev")
@RequiredArgsConstructor
public class CarrierService {

    private final CarrierRepository carrierRepository;

    public void save(Carrier carrier) throws Exception {
        carrierRepository.save(carrier);
    }

    public List<Carrier> getAllCarrier() {
        return (List<Carrier>) carrierRepository.findAll();
    }
}
