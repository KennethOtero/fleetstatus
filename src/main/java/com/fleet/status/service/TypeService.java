package com.fleet.status.service;

import com.fleet.status.dao.repository.TypeRepository;
import com.fleet.status.entity.Type;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TypeService {

    private final TypeRepository typeRepository;

    public List<Type> findAll() {
        return (List<Type>) typeRepository.findAll();
    }
}
