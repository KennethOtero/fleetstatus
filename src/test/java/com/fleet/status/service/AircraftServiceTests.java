package com.fleet.status.service;

import com.fleet.status.dao.repository.AircraftRepository;
import com.fleet.status.entity.Aircraft;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

public class AircraftServiceTests {

    @InjectMocks
    private AircraftService aircraftService;

    @Mock
    private AircraftRepository aircraftRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testSave() {
        Aircraft aircraft = new Aircraft();
        aircraft.setAircraftId(1L);
        when(aircraftRepository.save(any(Aircraft.class))).thenReturn(aircraft);

        aircraftService.save(aircraft);

        verify(aircraftRepository, times(1)).save(aircraft);
    }

    @Test
    public void testDeleteAircraft() {
        int aircraftId = 1;
        doNothing().when(aircraftRepository).deleteAircraft(aircraftId);

        aircraftService.deleteAircraft(aircraftId);

        verify(aircraftRepository, times(1)).deleteAircraft(aircraftId);
    }

    @Test
    public void testFindAll() {
        Aircraft aircraft1 = new Aircraft();
        aircraft1.setAircraftId(1L);
        Aircraft aircraft2 = new Aircraft();
        aircraft2.setAircraftId(2L);
        List<Aircraft> aircraftList = Arrays.asList(aircraft1, aircraft2);
        when(aircraftRepository.findAll()).thenReturn(aircraftList);

        List<Aircraft> result = aircraftService.findAll();

        assertEquals(2, result.size());
        verify(aircraftRepository, times(1)).findAll();
    }
}
