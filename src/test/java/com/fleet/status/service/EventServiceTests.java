package com.fleet.status.service;

import com.fleet.status.dao.EventDAO;
import com.fleet.status.entity.Aircraft;
import com.fleet.status.entity.Event;
import com.github.fge.jsonpatch.JsonPatch;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import org.assertj.core.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class EventServiceTests {

    @InjectMocks
    private EventService eventService;

    @Mock
    private EventDAO eventDAO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testShowBackInService() {
        Event event = new Event();
        event.setEventId(1L);
        Instant backInService = Instant.now();

        when(eventDAO.findById(Math.toIntExact(event.getEventId()))).thenReturn(event);
        doNothing().when(eventDAO).save(event);

        eventService.showBackInService(Math.toIntExact(event.getEventId()), backInService);

        verify(eventDAO, times(1)).save(event);
    }

    @Test
    void testShowBackInServiceNotFound() {
        Event event = new Event();
        event.setEventId(1L);
        Instant backInService = Instant.now();

        when(eventDAO.findById(Math.toIntExact(event.getEventId()))).thenReturn(null);

        eventService.showBackInService(Math.toIntExact(event.getEventId()), backInService);

        verify(eventDAO, times(0)).save(event);
    }

    @Test
    public void testUpdateEvent() {
        String eventId = "1";
        JsonPatch patch = mock(JsonPatch.class);
        Event eventToUpdate = new Event();
        when(eventDAO.findById(Integer.parseInt(eventId))).thenReturn(eventToUpdate);

        eventService.updateEvent(patch, eventId);

        verify(eventDAO, times(1)).save(any());
    }

    @Test
    public void testUpdateEvent_EventNotFound() {
        String eventId = "1";
        JsonPatch patch = mock(JsonPatch.class);
        when(eventDAO.findById(Integer.parseInt(eventId))).thenReturn(null);

        HttpClientErrorException exception = assertThrows(HttpClientErrorException.class, () ->
                eventService.updateEvent(patch, eventId));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        verify(eventDAO, times(0)).save(any());
    }

    @Test
    public void testGenerateCsv() {
        Integer carrierId = 1;
        Integer typeId = 2;
        String tailNumber = "N767AX";
        List<Integer> reasonIds = List.of(1, 2, 3);
        LocalDateTime startDate = LocalDateTime.of(2023, 1, 1, 0, 0);
        LocalDateTime endDate = LocalDateTime.of(2023, 12, 31, 23, 59);

        Event event = new Event();
        Aircraft aircraft = new Aircraft();
        aircraft.setTailNumber("N747AX");
        event.setAircraft(aircraft);
        List<Event> mockEvents = List.of(event);

        when(eventService.getFilteredEvents(carrierId, typeId, tailNumber, reasonIds, startDate, endDate)).thenReturn(mockEvents);

        byte[] csvData = eventService.generateCsv(carrierId, typeId, tailNumber, reasonIds, startDate, endDate);

        assertNotNull(csvData);
        assertTrue(csvData.length > 0);
    }

    @Test
    public void testGenerateCsv_Exception() {
        Integer carrierId = 1;
        Integer typeId = 2;
        String tailNumber = "N767AX";
        List<Integer> reasonIds = List.of(1, 2, 3);
        LocalDateTime startDate = LocalDateTime.of(2023, 1, 1, 0, 0);
        LocalDateTime endDate = LocalDateTime.of(2023, 12, 31, 23, 59);

        when(eventService.getFilteredEvents(carrierId, typeId, tailNumber, reasonIds, startDate, endDate)).thenThrow(new RuntimeException("Test Exception"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            eventService.generateCsv(carrierId, typeId, tailNumber, reasonIds, startDate, endDate);
        });

        assertEquals("Test Exception", exception.getMessage());
    }

    @Test
    public void testGenerateDowntimeReport() {
        Integer carrierId = 1;
        Integer typeId = 2;
        String tailNumber = "ABC123";
        List<Integer> reasonIds = List.of(1, 2, 3);
        Instant startDate = Instant.now();
        Instant nextUpdate = Instant.now().plusSeconds(100L);
        Instant endDate = Instant.now().plusSeconds(200L);

        Event event = new Event();
        Aircraft aircraft = new Aircraft();
        aircraft.setTailNumber("ABC123");
        event.setAircraft(aircraft);
        event.setStartTime(startDate);
        event.setEndTime(endDate);
        event.setReasonString("Maintenance");
        event.setRemark("Routine check");
        event.setBackInService(1);
        event.setNextUpdate(nextUpdate);
        List<Event> mockEvents = List.of(event);

        LocalDateTime startTime = LocalDateTime.of(2023, 1, 1, 0, 0);
        LocalDateTime endTime = LocalDateTime.of(2023, 12, 31, 23, 59);

        when(eventService.getFilteredEvents(carrierId, typeId, tailNumber, reasonIds, startTime, endTime)).thenReturn(mockEvents);

        byte[] csvData = eventService.generateDowntimeReport(carrierId, typeId, tailNumber, reasonIds, startTime, endTime);

        assertNotNull(csvData);
        assertTrue(csvData.length > 0);
    }

    @Test
    public void testGenerateDowntimeReport_Exception() {
        Integer carrierId = 1;
        Integer typeId = 2;
        String tailNumber = "ABC123";
        List<Integer> reasonIds = List.of(1, 2, 3);
        LocalDateTime startDate = LocalDateTime.of(2023, 1, 1, 0, 0);
        LocalDateTime endDate = LocalDateTime.of(2023, 12, 31, 23, 59);

        when(eventService.getFilteredEvents(carrierId, typeId, tailNumber, reasonIds, startDate, endDate)).thenThrow(new RuntimeException("Test Exception"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            eventService.generateDowntimeReport(carrierId, typeId, tailNumber, reasonIds, startDate, endDate);
        });

        assertEquals("Test Exception", exception.getMessage());
    }

    @Test
    void testGenerateRandomColor() {
        String result = eventService.generateRandomColor();
        assertNotNull(result);
    }
}
