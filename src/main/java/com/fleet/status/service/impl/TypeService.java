package com.fleet.status.service.impl;

import com.fleet.status.dao.impl.TypeDAO;
import com.fleet.status.entity.Type;
import com.fleet.status.service.ITypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Profile("dev")
@RequiredArgsConstructor
public class TypeService implements ITypeService {

    private final TypeDAO typeDAO;

    @Override
    public void save(Type type) {
        typeDAO.save(type);
    }

    @Override
    public List<Type> findAll() {
        return typeDAO.findAll();
    }
}
