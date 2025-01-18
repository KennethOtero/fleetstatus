package com.fleet.status.service.impl;

import com.fleet.status.dao.impl.TypeDAO;
import com.fleet.status.entity.Type;
import com.fleet.status.service.ITypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Profile("dev")
public class TypeService implements ITypeService {

    @Autowired
    private TypeDAO typeDAO;

    @Override
    public void save(Type type) {
        typeDAO.save(type);
    }

    @Override
    public List<Type> findAll() {
        return typeDAO.findAll();
    }
}
