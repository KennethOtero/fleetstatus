package com.fleet.status.dao.repository;

import com.fleet.status.dto.Type;
import org.springframework.context.annotation.Profile;
import org.springframework.data.repository.CrudRepository;

@Profile("!test")
public interface TypeRepository extends CrudRepository<Type, Integer> {
}
