package com.fleet.status.service;
import com.fleet.status.entity.Reason;

import java.util.List;

public interface IReasonService {
    void save (Reason reason) throws Exception;

    Reason findReasonById(int id);

    List<Reason> getAllReason();
}
