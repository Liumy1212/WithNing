package com.lmy.withning.service.impl;

import com.lmy.withning.mapper.AuthMapper;
import com.lmy.withning.pojo.dto.UserDto;
import com.lmy.withning.pojo.entity.User;
import com.lmy.withning.pojo.vo.UserVo;
import com.lmy.withning.service.AuthService;
import com.lmy.withning.utils.UserContextHolder;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private AuthMapper authMapper;

    public UserVo login(UserDto userDto){
        User user = authMapper.login(userDto);

        //TODO 临时thread方法
        UserContextHolder.setUser(user);

        UserVo userVo = new UserVo();
        BeanUtils.copyProperties(user,userVo);
        return userVo;
    }
}
