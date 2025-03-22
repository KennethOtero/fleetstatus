package com.fleet.status.service;

import com.fleet.status.dao.repository.ReasonRepository;
import com.fleet.status.entity.Reason;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest
public class ReasonServiceTests {

    @InjectMocks
    private ReasonService reasonService;

    @Mock
    private ReasonRepository reasonRepository;

    @Test
    void testGetAllReasons() {
        Reason reason1 = new Reason();
        Reason reason2 = new Reason();
        List<Reason> reasons = Arrays.asList(reason1, reason2);
        when(reasonRepository.findAll()).thenReturn(reasons);

        List<Reason> result = reasonService.getAllReason();

        assertEquals(2, result.size());
        verify(reasonRepository, times(1)).findAll();
    }
}
