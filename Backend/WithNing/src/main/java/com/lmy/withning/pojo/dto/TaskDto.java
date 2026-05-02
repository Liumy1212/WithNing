package com.lmy.withning.pojo.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TaskDto {
    private String title;
    private String description;
    private Integer type;
    private Integer rewardPoints;
    private LocalDateTime deadline;
    private String imageUrl;
}
