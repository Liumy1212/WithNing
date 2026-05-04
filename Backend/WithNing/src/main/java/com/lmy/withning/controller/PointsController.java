package com.lmy.withning.controller;

import com.lmy.withning.common.Result.Result;
import com.lmy.withning.pojo.entity.PointRecord;
import com.lmy.withning.service.PointsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/points")
@Slf4j
public class PointsController {

    @Autowired
    private PointsService pointsService;

    @GetMapping
    public Result getUserPoints() {
        log.info("获取用户积分");
        Integer points = pointsService.getUserPoints();
        return Result.success(points);
    }

    @GetMapping("/records")
    public Result getPointRecords() {
        log.info("获取积分记录");
        List<PointRecord> list = pointsService.getPointRecords();
        return Result.success(list);
    }
}
