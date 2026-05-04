package com.lmy.withning.pojo.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TaskPublishApplyVo {
    private Integer id;
    private Integer userId;
    private String userNickname;
    private String title;
    private String description;
    private Integer type;
    private Integer rewardPoints;
    private LocalDateTime deadline;
    private String imageUrl;
    private Integer status;
    private LocalDateTime createTime;
}
