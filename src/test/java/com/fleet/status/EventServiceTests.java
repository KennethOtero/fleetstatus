package com.fleet.status;

import com.fleet.status.service.EventService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class EventServiceTests {

    @InjectMocks
    private EventService eventService;

    @Test
    public void testCalculateDownTime() {
        String startTime = "2025-01-18 10:00:00";
        String endTime = "2025-01-18 15:00:00";
        long expectedDownTime = 5;

        long actualDownTime = eventService.calculateDownTime(startTime, endTime);

        assertEquals(expectedDownTime, actualDownTime);
    }

    @Test
    public void testCalculateDownTimeWithSeconds() {
        String startTime = "2025-01-18 10:00:00.123";
        String endTime = "2025-01-18 15:00:00.456";
        long expectedDownTime = 5;

        long actualDownTime = eventService.calculateDownTime(startTime, endTime);

        assertEquals(expectedDownTime, actualDownTime);
    }

    @Test
    public void testCalculateDownTimeNegativeDuration() {
        String startTime = "2025-01-18 15:00:00";
        String endTime = "2025-01-18 10:00:00";
        long expectedDownTime = 5;

        long actualDownTime = eventService.calculateDownTime(startTime, endTime);

        assertEquals(expectedDownTime, actualDownTime);
    }
}
