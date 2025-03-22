package com.fleet.status.controller;

import com.fleet.status.service.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.mock;

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
    void testGetHomepageEvents() throws Exception {}

    @Test
    void testGetOutOfServiceEvents() throws Exception {}

    @Test
    void testGetHistory() throws Exception {}

    @Test
    void testSaveEvent() throws Exception {}

    @Test
    void testShowBackInService() throws Exception {}

    @Test
    void testFindEvent() throws Exception {}

    @Test
    void testEditEvent() throws Exception {}

    @Test
    void testGetReasons() throws Exception {}

    @Test
    void testExportCSV() throws Exception {}

    @Test
    void testGetDownTimeReport() throws Exception {}

    @Test
    void testGetCalendarHistory() throws Exception {}

}
