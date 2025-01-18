package com.fleet.status.dao;

import com.fleet.status.entity.Reason;

import java.util.List;

public interface IReasonDAO {
    void save(Reason reason) throws Exception;

    Reason findById(int id);

    List<Reason> getAllReason();
}
