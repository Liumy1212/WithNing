package com.lmy.withning.pojo.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class User {
    private Integer id;
    private String username;
    private String password;
    private String nickname;
    private String email;
    private String phone;
    private String role;
    private Integer points;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
