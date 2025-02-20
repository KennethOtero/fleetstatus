package com.fleet.status.dao.repository;

import com.fleet.status.entity.Reason;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

@Profile("!test")
public interface ReasonRepository extends CrudRepository<Reason, Integer> {
    @Procedure(procedureName = "uspGetReasonsForEvent")
    List<Reason> getReasonsForEvent(@Param("intEventId") Long eventId);
}
