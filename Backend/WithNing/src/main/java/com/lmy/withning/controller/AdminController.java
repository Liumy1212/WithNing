package com.lmy.withning.controller;

import com.lmy.withning.common.Result.Result;
import com.lmy.withning.utils.UserContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
@Slf4j
public class AdminController {

    @GetMapping("/check")
    public Result check() {
        log.info("UserContextHolder: {}", UserContextHolder.getUser());
        boolean isAdmin = UserContextHolder.getUser().getRole().equals("1");
        if (!isAdmin){
            return Result.error("小小cyn还敢越级");
        }
        return Result.success();
    }
}
