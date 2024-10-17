package com.fleet.status.dao.repository;

import com.fleet.status.dto.Reason;
import org.springframework.context.annotation.Profile;
import org.springframework.data.repository.CrudRepository;

@Profile("!test")
public interface ReasonRepository extends CrudRepository<Reason, Integer> {
}
