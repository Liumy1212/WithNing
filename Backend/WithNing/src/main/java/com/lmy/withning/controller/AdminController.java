package com.lmy.withning.controller;

import com.lmy.withning.common.Result.Result;
import com.lmy.withning.pojo.entity.User;
import com.lmy.withning.pojo.vo.TaskCompleteApplyVo;
import com.lmy.withning.pojo.vo.TaskPublishApplyVo;
import com.lmy.withning.service.AdminService;
import com.lmy.withning.utils.UserContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@Slf4j
public class AdminController {

    @Autowired
    private AdminService adminService;

    @GetMapping("/check")
    public Result check() {
        log.info("UserContextHolder: {}", UserContextHolder.getUser());
        boolean isAdmin = UserContextHolder.getUser().getRole().equals("1");
        if (!isAdmin) {
            return Result.error("小小cyn还敢越级");
        }
        return Result.success();
    }

    @GetMapping("/tasks/publish/applies")
    public Result getTaskPublishApplies() {
        log.info("获取任务发布申请列表");
        List<TaskPublishApplyVo> list = adminService.getTaskPublishApplies();
        return Result.success(list);
    }

    @PutMapping("/tasks/publish/{taskId}/review")
    public Result reviewTaskPublish(@PathVariable Long taskId, @RequestBody Map<String, Integer> request) {
        log.info("审核任务发布申请，taskId: {}", taskId);
        adminService.reviewTaskPublish(taskId, request.get("status"));
        return Result.success();
    }

    @GetMapping("/tasks/applies")
    public Result getTaskCompleteApplies() {
        log.info("获取任务完成申请列表");
        List<TaskCompleteApplyVo> list = adminService.getTaskCompleteApplies();
        return Result.success(list);
    }

    @PutMapping("/tasks/apply/{applyId}/review")
    public Result reviewTaskComplete(@PathVariable Long applyId, @RequestBody Map<String, Integer> request) {
        log.info("审核任务完成申请，applyId: {}", applyId);
        adminService.reviewTaskComplete(applyId, request.get("status"));
        return Result.success();
    }

    @PutMapping("/tasks/{taskId}/complete")
    public Result completeTaskDirectly(@PathVariable Long taskId) {
        log.info("管理员直接完成任务，taskId: {}", taskId);
        adminService.completeTaskDirectly(taskId);
        return Result.success();
    }

    @GetMapping("/users")
    public Result getAllUsers() {
        log.info("获取所有用户列表");
        List<User> list = adminService.getAllUsers();
        return Result.success(list);
    }
}

