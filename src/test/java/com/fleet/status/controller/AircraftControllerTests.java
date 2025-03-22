package com.fleet.status.controller;

import com.fleet.status.config.UriConstants;
import com.fleet.status.entity.Aircraft;
import com.fleet.status.entity.Carrier;
import com.fleet.status.entity.Type;
import com.fleet.status.service.AircraftService;
import com.fleet.status.service.CarrierService;
import com.fleet.status.service.TypeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class AircraftControllerTests {

    private MockMvc         mockMvc;
    private AircraftService aircraftService;
    private CarrierService  carrierService;
    private TypeService     typeService;

    @BeforeEach
    void setUp() {
        aircraftService = mock(AircraftService.class);
        carrierService = mock(CarrierService.class);
        typeService = mock(TypeService.class);

        AircraftController aircraftController = new AircraftController(aircraftService, carrierService, typeService);

        mockMvc = MockMvcBuilders.standaloneSetup(aircraftController).build();
    }

    @Test
    void testRemoveAircraft() throws Exception {
        int aircraftId = 1;
        doNothing().when(aircraftService).deleteAircraft(aircraftId);

        mockMvc.perform(delete(UriConstants.URI_AIRCRAFT)
                        .param("aircraftId", String.valueOf(aircraftId))
                        .accept(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk())
                        .andExpect(content().string("Aircraft deleted."));
    }

    @Test
    void testRemoveAircraftNotFound() throws Exception {
        int aircraftId = 1;
        doThrow(RuntimeException.class).when(aircraftService).deleteAircraft(aircraftId);

        mockMvc.perform(delete(UriConstants.URI_AIRCRAFT)
                        .param("aircraftId", String.valueOf(aircraftId))
                        .accept(MediaType.APPLICATION_JSON))
                        .andExpect(status().isInternalServerError());
    }

    @Test
    void testGetAllAircraft() throws Exception {
        List<Aircraft> aircraftList = Arrays.asList(new Aircraft(), new Aircraft());
        when(aircraftService.findAll()).thenReturn(aircraftList);

        mockMvc.perform(get(UriConstants.URI_AIRCRAFT)
                        .accept(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void testSaveAircraft() throws Exception {
        doNothing().when(aircraftService).save(any(Aircraft.class));

        mockMvc.perform(post(UriConstants.URI_AIRCRAFT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"field\": \"value\"}")
                        .accept(MediaType.APPLICATION_JSON))
                        .andExpect(status().isCreated())
                        .andExpect(content().string("Aircraft saved."));
    }

    @Test
    void testSavingExistingAircraft() throws Exception {
        doThrow(DataIntegrityViolationException.class).when(aircraftService).save(any(Aircraft.class));

        mockMvc.perform(post(UriConstants.URI_AIRCRAFT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"field\": \"value\"}")
                        .accept(MediaType.APPLICATION_JSON))
                        .andExpect(status().isConflict());
    }

    @Test
    void testSavingAircraftGenericError() throws Exception {
        doThrow(RuntimeException.class).when(aircraftService).save(any(Aircraft.class));

        mockMvc.perform(post(UriConstants.URI_AIRCRAFT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"field\": \"value\"}")
                        .accept(MediaType.APPLICATION_JSON))
                        .andExpect(status().isInternalServerError());
    }

    @Test
    void testGetAllCarriers() throws Exception {
        List<Carrier> carrierList = Arrays.asList(new Carrier(), new Carrier());
        when(carrierService.getAllCarrier()).thenReturn(carrierList);

        mockMvc.perform(get(UriConstants.URI_CARRIER)
                        .accept(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void testGetAllTypes() throws Exception {
        List<Type> typeList = Arrays.asList(new Type(), new Type());
        when(typeService.findAll()).thenReturn(typeList);

        mockMvc.perform(get(UriConstants.URI_TYPE)
                        .accept(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.length()").value(2));
    }
}
