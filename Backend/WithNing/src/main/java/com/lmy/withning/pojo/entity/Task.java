package com.lmy.withning.pojo.entity;

import lombok.Data;

import java.math.BigInteger;
import java.time.LocalDateTime;

@Data
public class Task {
    private Integer id;
    private String title;
    private String description;
    private String type;
    private Integer rewardPoints;
    private LocalDateTime deadline;
    private String imageUrl;
    private Long userId;
    private String status;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
