package com.fleet.status.service;

import com.fleet.status.dao.repository.ReasonRepository;
import com.fleet.status.entity.Reason;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class ReasonServiceTests {

    @InjectMocks
    private ReasonService reasonService;

    @Mock
    private ReasonRepository reasonRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

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
