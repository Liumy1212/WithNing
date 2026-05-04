package com.lmy.withning.pojo.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ExchangeApplyVo {
    private Integer id;
    private Integer userId;
    private String userNickname;
    private Integer productId;
    private String productName;
    private Integer price;
    private Integer status;
    private LocalDateTime createTime;
}
