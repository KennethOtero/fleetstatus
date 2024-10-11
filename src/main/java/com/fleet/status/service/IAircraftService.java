package com.fleet.status.service;

import com.fleet.status.dto.Aircraft;

import java.util.List;

public interface IAircraftService {

    Aircraft fetchById(int id);

    void save(Aircraft aircraft) throws Exception;

    List<Aircraft> getOutofServiceAircraft();

    List<Aircraft> getAllAircraft();

    List<Aircraft> getOutOfServiceAircraft();

    List<Aircraft> getInServiceAircraft();

    List<Aircraft> getAllAircraftFromCarrier(int carrierId);

    List<Aircraft> getAllAircraftFromCarrierOOS(int carrierId);

    List<Aircraft> getAllAircraftFromCarrierIS(int carrierId);

    Aircraft updateAircraftServiceStatus(int aircraftId, int backInService);

    void deleteAircraft(int aircraftId);

    long calculateDownTime(String startTime, String endTime);
}
