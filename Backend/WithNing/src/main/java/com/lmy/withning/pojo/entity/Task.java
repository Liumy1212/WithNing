package com.lmy.withning.pojo.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Task {
    private Integer id;
    private String title;
    private String description;
    private Integer type;
    private Integer rewardPoints;
    private LocalDateTime deadline;
    private String imageUrl;
    private Integer publisherId;
    private Integer status;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
