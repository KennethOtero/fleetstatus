package com.fleet.status.dao.repository;

import com.fleet.status.entity.Event;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

@Profile("!test")
public interface EventRepository extends CrudRepository<Event, Integer> {
    @Query(value = "SELECT * FROM vAllAircraft WHERE [blnBackInService] = 0 OR (dtmEndTime IS NOT NULL AND DATEDIFF(minute, dtmEndTime, GETUTCDATE()) < 30)", nativeQuery = true)
    List<Event> getHomepageAircraft();

    @Query(value = "SELECT * FROM vOutOfServiceAircraft", nativeQuery = true)
    List<Event> getOutOfServiceAircraft();
}
