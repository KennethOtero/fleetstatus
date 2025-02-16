package com.fleet.status.service.impl;

import com.fleet.status.dao.repository.ReasonRepository;
import com.fleet.status.entity.Reason;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@Profile("dev")
@RequiredArgsConstructor
public class ReasonService {

    private final ReasonRepository reasonRepository;

    public List<Reason> getAllReason() {
        return (List<Reason>) reasonRepository.findAll();
    }
}
