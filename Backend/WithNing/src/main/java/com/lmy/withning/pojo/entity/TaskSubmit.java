package com.lmy.withning.pojo.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TaskSubmit {
    private Integer id;
    private Integer userId;
    private String title;
    private String description;
    private Integer type;
    private Integer rewardPoints;
    private LocalDateTime deadline;
    private String imageUrl;
    private Integer status;
    private Integer taskId;
    private Integer reviewerId;
    private String reviewComment;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private LocalDateTime reviewTime;
}

