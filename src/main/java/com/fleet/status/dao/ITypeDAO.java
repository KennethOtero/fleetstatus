package com.fleet.status.dao;

import com.fleet.status.dto.Type;

import java.util.List;

public interface ITypeDAO {
    void save(Type type);

    List<Type> findAll();
}
