package com.lmy.withning.pojo.vo;

import lombok.Data;

@Data
public class UserVo {
    private Integer id;
    private String username;
    private String nickname;
    private String role;
    private Integer points;
}
