package com.lmy.withning.service;

import com.lmy.withning.pojo.entity.PointRecord;

import java.util.List;

public interface PointsService {
    Integer getUserPoints();
    List<PointRecord> getPointRecords();
}
