package com.lmy.withning.pojo.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserTask {
    private Integer id;
    private Integer userId;
    private Integer taskId;
    private Boolean completed;
    private LocalDateTime completeTime;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
