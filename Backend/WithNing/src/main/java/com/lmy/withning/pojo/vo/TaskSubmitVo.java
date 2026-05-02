package com.lmy.withning.pojo.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TaskSubmitVo {
    private Integer id;
    private Integer userId;
    private Integer taskId;
    private String title;
    private Integer rewardPoints;
    private Integer status;
    private LocalDateTime createTime;
}
