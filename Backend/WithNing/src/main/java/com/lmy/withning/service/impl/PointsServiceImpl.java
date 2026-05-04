package com.lmy.withning.service.impl;

import com.lmy.withning.mapper.PointsMapper;
import com.lmy.withning.pojo.entity.PointRecord;
import com.lmy.withning.service.PointsService;
import com.lmy.withning.utils.UserContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class PointsServiceImpl implements PointsService {

    @Autowired
    private PointsMapper pointsMapper;

    @Override
    public Integer getUserPoints() {
        log.info("获取用户积分");
        return UserContextHolder.getUser().getPoints();
    }

    @Override
    public List<PointRecord> getPointRecords() {
        log.info("获取用户积分记录");
        Integer userId = UserContextHolder.getUser().getId();
        return pointsMapper.getPointRecords(userId);
    }
}
