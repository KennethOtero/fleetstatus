package com.fleet.status.dao;

import com.fleet.status.dao.repository.EventRepository;
import com.fleet.status.dao.repository.ReasonRepository;
import com.fleet.status.entity.Event;
import com.fleet.status.entity.Reason;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class EventDAOTests {

    @InjectMocks
    private EventDAO eventDAO;

    @Mock
    private EventRepository eventRepository;

    @Mock
    private ReasonRepository reasonRepository;

    @Mock
    private EntityManager entityManager;

    @Mock
    private Query query;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSaveEvent() {
        when(eventRepository.save(Mockito.any(Event.class))).thenReturn(new Event());

        eventDAO.save(new Event());

        verify(eventRepository, times(1)).save(Mockito.any(Event.class));
    }

    @Test
    void testFindEventById() {
        Event event = new Event();
        event.setEventId(1L);

        when(eventRepository.findById(anyInt())).thenReturn(Optional.of(event));

        Event foundEvent = eventDAO.findById(Math.toIntExact(event.getEventId()));

        verify(eventRepository, times(1)).findById(anyInt());
        assertEquals(event.getEventId(), foundEvent.getEventId());
    }

    @Test
    void testGetHomepageEvents() {
        Event event = new Event();
        List<Event> events = new ArrayList<>();
        events.add(event);

        when(eventRepository.getHomepageAircraft()).thenReturn(events);

        List<Event> foundEvents = eventDAO.getHomepageEvents();

        verify(eventRepository, times(1)).getHomepageAircraft();
        assertNotNull(foundEvents);
    }

    @Test
    void testGetOutOfServiceEvents() {
        Event event = new Event();
        List<Event> events = new ArrayList<>();
        events.add(event);

        when(eventRepository.getOutOfServiceAircraft()).thenReturn(events);

        List<Event> foundEvents = eventDAO.getOutOfServiceEvents();

        verify(eventRepository, times(1)).getOutOfServiceAircraft();
        assertNotNull(foundEvents);
    }

    @Test
    public void testGetFilteredEvents() {
        Integer carrierId = 1;
        Integer typeId = 2;
        String tailNumber = "N767AX";
        List<Integer> reasonIds = List.of(1, 2, 3);
        LocalDateTime startDate = LocalDateTime.of(2023, 1, 1, 0, 0);
        LocalDateTime endDate = LocalDateTime.of(2023, 12, 31, 23, 59);
        List<Object[]> mockResults = new ArrayList<>();

        when(entityManager.createNativeQuery(anyString())).thenReturn(query);
        when(query.getResultList()).thenReturn(mockResults);
        when(reasonRepository.findById(anyInt())).thenReturn(Optional.of(new Reason()));

        List<Event> events = eventDAO.getFilteredEvents(carrierId, typeId, tailNumber, reasonIds, startDate, endDate);

        assertNotNull(events);
    }

    @Test
    public void testGetFilteredEventsWithError() {
        Integer carrierId = 1;
        Integer typeId = 2;
        String tailNumber = "N767AX";
        List<Integer> reasonIds = List.of(1, 2, 3);
        LocalDateTime startDate = LocalDateTime.of(2023, 1, 1, 0, 0);
        LocalDateTime endDate = LocalDateTime.of(2023, 12, 31, 23, 59);

        when(entityManager.createNativeQuery(anyString())).thenThrow(new RuntimeException());

        assertThrows(RuntimeException.class, () -> eventDAO.getFilteredEvents(carrierId, typeId, tailNumber, reasonIds, startDate, endDate));
    }

}
