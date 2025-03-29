package com.fleet.status.service;

import com.fleet.status.dao.repository.TypeRepository;
import com.fleet.status.entity.Type;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class TypeServiceTests {

    @InjectMocks
    public TypeService typeService;

    @Mock
    public TypeRepository typeRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindAllTypes() {
        Type type1 = new Type();
        Type type2 = new Type();
        List<Type> reasons = Arrays.asList(type1, type2);

        when(typeRepository.findAll()).thenReturn(reasons);

        List<Type> result = typeService.findAll();

        assertEquals(2, result.size());
        verify(typeRepository, times(1)).findAll();
    }
}
