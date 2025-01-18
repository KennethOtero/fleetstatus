package com.fleet.status.service.impl;

import com.fleet.status.dao.IReasonDAO;
import com.fleet.status.entity.Reason;
import com.fleet.status.service.IReasonService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@Profile("dev")
@RequiredArgsConstructor
public class ReasonService implements IReasonService {

    private final IReasonDAO reasonDAO;

    @Override
    public void save(Reason reason) throws Exception {

    }

    @Override
    public Reason findReasonById(int id) {
        return null;
    }

    @Override
    public List<Reason> getAllReason() {
        return reasonDAO.getAllReason();
    }
}
