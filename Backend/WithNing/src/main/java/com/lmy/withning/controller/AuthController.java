package com.lmy.withning.controller;

import com.fasterxml.jackson.databind.util.BeanUtil;
import com.lmy.withning.common.Result.Result;
import com.lmy.withning.pojo.dto.UserDto;
import com.lmy.withning.pojo.entity.User;
import com.lmy.withning.pojo.vo.UserVo;
import com.lmy.withning.service.AuthService;
import com.lmy.withning.utils.UserContextHolder;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@Slf4j
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public Result login(@RequestBody UserDto userDto, HttpSession session){
        log.info("用户登录");
        UserVo userVo = authService.login(userDto);
//        session.setAttribute("user", UserContextHolder.getUser());
        log.info("当前用户：{}",UserContextHolder.getUser());
        return Result.success(userVo);
    }

    @PostMapping("/logout")
    public Result logout(){
        log.info("用户登出");
        UserContextHolder.remove();
        return Result.success();
    }
}
