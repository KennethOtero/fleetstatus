package com.fleet.status.dao.stub;

import com.fleet.status.dao.ICarrierDAO;
import com.fleet.status.dto.Carrier;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@Profile("test")
public class CarrierDAOStub implements ICarrierDAO {
    // Store locally
    Map<Long, Carrier> carrierRepository = new HashMap<>();

    @Override
    public void save(Carrier carrier) {
        carrierRepository.put(carrier.getCarrierId(), carrier);
    }

    @Override
    public Carrier findById(int id) {
        return carrierRepository.get((long) id);
    }

    @Override
    public List<Carrier> getAllCarrier() {
        return new ArrayList<>(carrierRepository.values());
    }
}
