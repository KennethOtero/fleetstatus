package com.fleet.status.service;

import com.fleet.status.dao.repository.ReasonRepository;
import com.fleet.status.entity.Reason;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReasonService {

    private final ReasonRepository reasonRepository;

    public List<Reason> getAllReason() {
        return (List<Reason>) reasonRepository.findAll();
    }
}
