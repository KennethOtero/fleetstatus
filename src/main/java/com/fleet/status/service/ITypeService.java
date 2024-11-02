package com.fleet.status.service;

import com.fleet.status.dto.Type;

import java.util.List;

public interface ITypeService {
    void save(Type type);

    List<Type> findAll();
}
