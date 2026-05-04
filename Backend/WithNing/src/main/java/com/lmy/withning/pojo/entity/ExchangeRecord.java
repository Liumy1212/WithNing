package com.lmy.withning.pojo.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ExchangeRecord {
    private Integer id;
    private Integer userId;
    private Integer productId;
    private String productName;
    private Integer price;
    private LocalDateTime exchangeTime;
}
