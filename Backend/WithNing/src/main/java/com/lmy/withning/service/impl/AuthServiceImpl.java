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
    
    @Override
    public UserVo register(UserDto userDto) {
        // 检查用户名是否已存在
        User existUser = authMapper.findByUsername(userDto.getUsername());
        if (existUser != null) {
            throw new RuntimeException("用户名已存在");
        }
        
        User user = new User();
        BeanUtils.copyProperties(userDto, user);
        authMapper.register(user);
        
        // 重新查询获取完整信息
        User newUser = authMapper.findByUsername(userDto.getUsername());
        UserContextHolder.setUser(newUser);
        
        UserVo userVo = new UserVo();
        BeanUtils.copyProperties(newUser, userVo);
        return userVo;
    }
    
    @Override
    public UserVo getUserInfo() {
        User user = UserContextHolder.getUser();
        if (user == null) {
            throw new RuntimeException("用户未登录");
        }
        UserVo userVo = new UserVo();
        BeanUtils.copyProperties(user, userVo);
        return userVo;
    }
}
