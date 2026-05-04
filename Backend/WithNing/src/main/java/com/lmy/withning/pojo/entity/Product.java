package com.lmy.withning.pojo.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Product {
    private Integer id;
    private String name;
    private String description;
    private Integer price;
    private String imageUrl;
    private Integer stock;
    private Integer status;
    private Integer publisherId;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
