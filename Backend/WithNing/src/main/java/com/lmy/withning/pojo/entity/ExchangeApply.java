package com.lmy.withning.pojo.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ExchangeApply {
    private Integer id;
    private Integer userId;
    private Integer productId;
    private String productName;
    private Integer price;
    private Integer status;
    private Integer reviewerId;
    private String reviewComment;
    private LocalDateTime createTime;
    private LocalDateTime reviewTime;
}
