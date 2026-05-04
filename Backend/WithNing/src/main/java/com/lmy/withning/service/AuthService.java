package com.lmy.withning.service;

import com.lmy.withning.pojo.dto.UserDto;
import com.lmy.withning.pojo.entity.User;
import com.lmy.withning.pojo.vo.UserVo;

public interface AuthService {
    UserVo login(UserDto userdto);
    UserVo register(UserDto userDto);
    UserVo getUserInfo();
}
