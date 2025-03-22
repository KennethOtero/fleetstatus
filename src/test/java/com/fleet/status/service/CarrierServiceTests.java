package com.fleet.status.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.fleet.status.dao.repository.CarrierRepository;
import com.fleet.status.entity.Carrier;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

public class CarrierServiceTests {

    @InjectMocks
    private CarrierService carrierService;

    @Mock
    private CarrierRepository carrierRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllCarrier() {
        Carrier carrier1 = new Carrier();
        Carrier carrier2 = new Carrier();
        List<Carrier> carriers = Arrays.asList(carrier1, carrier2);
        when(carrierRepository.findAll()).thenReturn(carriers);

        List<Carrier> result = carrierService.getAllCarrier();

        assertEquals(2, result.size());
        verify(carrierRepository, times(1)).findAll();
    }
}
