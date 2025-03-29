package com.fleet.status.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fleet.status.config.UriConstants;
import com.fleet.status.entity.Aircraft;
import com.fleet.status.entity.Event;
import com.fleet.status.service.*;
import com.github.fge.jsonpatch.JsonPatch;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class EventControllerTests {

    private MockMvc         mockMvc;
    private EventService    eventService;
    private ReasonService   reasonService;

    @BeforeEach
    void setUp() {
        eventService = mock(EventService.class);
        reasonService = mock(ReasonService.class);

        EventController aircraftController = new EventController(eventService, reasonService);

        mockMvc = MockMvcBuilders.standaloneSetup(aircraftController).build();
    }

    @Test
    void testGetHomepageEvents() throws Exception {
        List<Event> events = new ArrayList<>();
        Event event = new Event();
        events.add(event);

        when(eventService.getHomepageEvents()).thenReturn(events);

        mockMvc.perform(get(UriConstants.URI_EVENTS))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isNotEmpty());
    }

    @Test
    void testGetHomepageEventsError() throws Exception {
        when(eventService.getHomepageEvents()).thenThrow(RuntimeException.class);

        mockMvc.perform(get(UriConstants.URI_EVENTS))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void testGetOutOfServiceEvents() throws Exception {
        List<Event> events = new ArrayList<>();
        Event event = new Event();
        events.add(event);

        when(eventService.getOutOfServiceEvents()).thenReturn(events);

        mockMvc.perform(get(UriConstants.URI_OOS_EVENTS))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isNotEmpty());
    }

    @Test
    void testGetOutOfServiceEventsError() throws Exception {
        when(eventService.getOutOfServiceEvents()).thenThrow(RuntimeException.class);

        mockMvc.perform(get(UriConstants.URI_OOS_EVENTS))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void testGetHistory() throws Exception {
        List<Event> events = new ArrayList<>();
        Event event = new Event();
        events.add(event);

        when(eventService.getFilteredEvents(any(), any(), any(), any(), any(), any())).thenReturn(events);

        mockMvc.perform(get(UriConstants.URI_EVENT_HISTORY))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isNotEmpty());
    }

    @Test
    void testGetHistoryError() throws Exception {
        when(eventService.getFilteredEvents(any(), any(), any(), any(), any(), any())).thenThrow(RuntimeException.class);

        mockMvc.perform(get(UriConstants.URI_EVENT_HISTORY))
                .andExpect(status().isInternalServerError());
    }

    @Test
    public void testSubmitEvent() throws Exception {
        Event event = new Event();
        ObjectMapper objectMapper = new ObjectMapper();

        mockMvc.perform(post(UriConstants.URI_EVENTS)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(event)))
                        .andExpect(status().isCreated());
    }

    @Test
    public void testSubmitEventConflict() throws Exception {
        Event event = new Event();
        ObjectMapper objectMapper = new ObjectMapper();

        doThrow(DataIntegrityViolationException.class).when(eventService).save(any());

        mockMvc.perform(post(UriConstants.URI_EVENTS)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(event)))
                        .andExpect(status().isConflict());
    }

    @Test
    public void testSubmitEventInternalServerError() throws Exception {
        Event event = new Event();
        Aircraft aircraft = new Aircraft();
        aircraft.setTailNumber("N767AX");

        event.setAircraft(aircraft);
        ObjectMapper objectMapper = new ObjectMapper();

        doThrow(RuntimeException.class).when(eventService).save(any());

        mockMvc.perform(post(UriConstants.URI_EVENTS)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(event)))
                        .andExpect(status().isInternalServerError());
    }

    @Test
    public void testShowBackInService() throws Exception {
        int eventId = 1;
        String backInServiceDate = "2025-03-22T20:42:12Z";

        mockMvc.perform(put(UriConstants.URI_SHOW_BACK_IN_SERVICE, eventId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(backInServiceDate))
                        .andExpect(status().isOk());
    }

    @Test
    public void testShowBackInServiceInternalServerError() throws Exception {
        int eventId = 1;
        String backInServiceDate = "invalid-date";

        mockMvc.perform(put(UriConstants.URI_SHOW_BACK_IN_SERVICE, eventId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(backInServiceDate))
                        .andExpect(status().isInternalServerError());
    }

    @Test
    void testFindEvent() throws Exception {
        int eventId = 1;
        mockMvc.perform(get(UriConstants.URI_EVENTS_EVENT_ID, eventId))
                        .andExpect(status().isOk());
    }

    @Test
    public void testEditEvent() throws Exception {
        String eventId = "1";
        String patchString = "[{\"op\":\"replace\",\"path\":\"/reason\",\"value\":\"selectedReasons\"}," +
                "{\"op\":\"replace\",\"path\":\"/nextUpdate\",\"value\":\"2025-03-22T20:42:12Z\"}," +
                "{\"op\":\"replace\",\"path\":\"/remark\",\"value\":\"remark\"}," +
                "{\"op\":\"replace\",\"path\":\"/startTime\",\"value\":\"2025-03-22T20:42:12Z\"}]";
        ObjectMapper objectMapper = new ObjectMapper();
        JsonPatch patch = objectMapper.readValue(patchString, JsonPatch.class);

        mockMvc.perform(patch(UriConstants.URI_EVENTS_EVENT_ID, eventId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(patch)))
                        .andExpect(status().isOk());
    }

    @Test
    public void testEditEventInternalServerError() throws Exception {
        String eventId = "1";
        String patchString = "[{\"op\":\"replace\",\"path\":\"/invalidPath\",\"value\":\"value\"}]";
        ObjectMapper objectMapper = new ObjectMapper();
        JsonPatch patch = objectMapper.readValue(patchString, JsonPatch.class);

        doThrow(RuntimeException.class).when(eventService).updateEvent(any(), any());

        mockMvc.perform(patch(UriConstants.URI_EVENTS_EVENT_ID, eventId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(patch)))
                        .andExpect(status().isInternalServerError());
    }

    @Test
    void testGetReasons() throws Exception {
        mockMvc.perform(get(UriConstants.URI_REASON))
                .andExpect(status().isOk());
    }

    @Test
    public void testExportCsv() throws Exception {
        mockMvc.perform(get(UriConstants.URI_CSV)
                        .param("carrierId", "1")
                        .param("typeId", "2")
                        .param("tailNumber", "ABC123")
                        .param("reasonIds", "1,2,3")
                        .param("startDate", "2025-03-22T20:42:12")
                        .param("endDate", "2025-03-23T20:42:12"))
                        .andExpect(status().isOk())
                        .andExpect(header().string(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=data.csv"))
                        .andExpect(header().string(HttpHeaders.CONTENT_TYPE, "text/csv"));
    }

    @Test
    void testGetDownTimeReport() throws Exception {
        mockMvc.perform(get(UriConstants.URI_DOWNTIME_REPORT)
                        .param("carrierId", "1")
                        .param("typeId", "2")
                        .param("tailNumber", "ABC123")
                        .param("reasonIds", "1,2,3")
                        .param("startDate", "2025-03-22T20:42:12")
                        .param("endDate", "2025-03-23T20:42:12"))
                        .andExpect(status().isOk())
                        .andExpect(header().string(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=DowntimeReport.csv"))
                        .andExpect(header().string(HttpHeaders.CONTENT_TYPE, "text/csv"));
    }

    @Test
    void testGetCalendarHistory() throws Exception {
        mockMvc.perform(get(UriConstants.URI_CALENDER_EVENT_HISTORY)
                        .param("carrierId", "1")
                        .param("typeId", "2")
                        .param("tailNumber", "ABC123")
                        .param("reasonIds", "1,2,3")
                        .param("startDate", "2025-03-22T20:42:12")
                        .param("endDate", "2025-03-23T20:42:12"))
                        .andExpect(status().isOk());
    }

    @Test
    void testGetCalendarHistoryInternalServerError() throws Exception {
        doThrow(RuntimeException.class).when(eventService).getCalendarEvents(any());

        mockMvc.perform(get(UriConstants.URI_CALENDER_EVENT_HISTORY)
                        .param("carrierId", "1")
                        .param("typeId", "2")
                        .param("tailNumber", "ABC123")
                        .param("reasonIds", "1,2,3")
                        .param("startDate", "2025-03-22T20:42:12")
                        .param("endDate", "2025-03-23T20:42:12"))
                        .andExpect(status().isInternalServerError());
    }

}
