package com.lmy.withning.pojo.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PointRecord {
    private Integer id;
    private Integer userId;
    private Integer points;
    private String description;
    private Integer type;
    private LocalDateTime createTime;
}
