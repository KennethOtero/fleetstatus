package com.fleet.status.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.fleet.status.dao.repository.CarrierRepository;
import com.fleet.status.entity.Carrier;
import org.springframework.boot.test.context.SpringBootTest;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Arrays;
import java.util.List;

@SpringBootTest
public class CarrierServiceTests {

    @Mock
    private CarrierRepository carrierRepository;

    @InjectMocks
    private CarrierService carrierService;

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
