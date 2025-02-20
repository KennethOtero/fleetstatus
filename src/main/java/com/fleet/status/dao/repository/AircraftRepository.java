package com.fleet.status.dao.repository;

import com.fleet.status.entity.Aircraft;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

@Profile("!test")
public interface AircraftRepository extends CrudRepository<Aircraft, Integer> {
    @Procedure(procedureName = "uspDeleteAircraft")
    void deleteAircraft(@Param("intAircraftId") int id);
}
