package com.fleet.status.service.impl;

import com.fleet.status.dao.impl.AircraftDAO;
import com.fleet.status.dto.Aircraft;
import com.fleet.status.service.IAircraftService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Profile("dev")
public class AircraftService implements IAircraftService {

    @Autowired
    private AircraftDAO aircraftDAO;

    private final List<Aircraft> outOfServiceAircraft = new ArrayList<>();

    public AircraftService() {
        Aircraft aircraftDTO = new Aircraft();
        aircraftDTO.setAircraftId(12);
        aircraftDTO.setTailNumber("N767AX");
        aircraftDTO.setReason("DAMAGED");
        aircraftDTO.setRemark("Bird strike to the #1 engine");
        aircraftDTO.setNextUpdate("13:21z");

        Aircraft aircraftDTO2 = new Aircraft();
        aircraftDTO2.setAircraftId(10);
        aircraftDTO2.setTailNumber("N650GT");
        aircraftDTO2.setReason("MAINTENANCE");
        aircraftDTO2.setRemark("#1 Generator inop");
        aircraftDTO2.setNextUpdate("15:00z");
        aircraftDTO2.setBackInService(1);

        Aircraft aircraftDTO3 = new Aircraft();
        aircraftDTO3.setAircraftId(10);
        aircraftDTO3.setTailNumber("N762CK");
        aircraftDTO3.setReason("AOG");
        aircraftDTO3.setRemark("Awaiting replacement FMC and required engineering order from Boeing");
        aircraftDTO3.setNextUpdate("21:00z");

        outOfServiceAircraft.add(aircraftDTO);
        outOfServiceAircraft.add(aircraftDTO2);
        outOfServiceAircraft.add(aircraftDTO3);
    }

    @Override
    public Aircraft fetchById(int id) {
        return outOfServiceAircraft.get(id);
    }

    @Override
    public void save(Aircraft aircraft) throws Exception {
        aircraftDAO.save(aircraft);
    }

    @Override
    public List<Aircraft> getOutofServiceAircraft() {
        return outOfServiceAircraft;
    }
}
