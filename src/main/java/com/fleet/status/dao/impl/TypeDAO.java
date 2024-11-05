package com.fleet.status.dao.impl;

import com.fleet.status.dao.ITypeDAO;
import com.fleet.status.dao.repository.TypeRepository;
import com.fleet.status.dto.Type;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.List;

@Slf4j
@Repository
@Profile("dev")
public class TypeDAO implements ITypeDAO {

    @Autowired
    private TypeRepository typeRepository;

    @Override
    public void save(Type type) {
        try {
            typeRepository.save(type);
            log.info("Saved type with ID {}", type.getTypeId());
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Type> findAll() {
        try {
            return (List<Type>) typeRepository.findAll();
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
