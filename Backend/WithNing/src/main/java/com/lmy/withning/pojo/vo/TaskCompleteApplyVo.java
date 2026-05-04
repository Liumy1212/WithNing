package com.lmy.withning.pojo.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TaskCompleteApplyVo {
    private Integer id;
    private Integer userId;
    private String userNickname;
    private Integer taskId;
    private String taskTitle;
    private Integer rewardPoints;
    private Integer status;
    private String imageUrl;
    private LocalDateTime createTime;
}
