package com.fleet.status.dao.repository;

import com.fleet.status.entity.User;
import org.springframework.context.annotation.Profile;
import org.springframework.data.repository.CrudRepository;

@Profile("!test")
public interface UserRepository extends CrudRepository<User, Integer> {
    User findByUsername(String username);
}
