package com.lmy.withning.controller;

import com.lmy.withning.common.Result.Result;
import com.lmy.withning.pojo.dto.TaskDto;
import com.lmy.withning.pojo.entity.Task;
import com.lmy.withning.pojo.vo.TaskSubmitVo;
import com.lmy.withning.service.TasksService;
import com.lmy.withning.utils.UserContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
@Slf4j
public class TasksController {

    @Autowired
    private TasksService tasksService;

    @GetMapping("/daily")
    public Result daily(){
        Long userId = Long.valueOf(UserContextHolder.getUser().getId());
        List<Task> list = tasksService.daily(userId);
        log.info("list: {}", list);
        return Result.success(list);
    }

    @GetMapping("/stage")
    public Result stage(){
        Long userId = Long.valueOf(UserContextHolder.getUser().getId());
        List<Task> list = tasksService.stage(userId);
        log.info("list: {}", list);
        return Result.success(list);
    }

    @GetMapping("/{taskId}")
    public Result getById(@PathVariable Long taskId){
        log.info("获取任务");
        Task task = tasksService.getById(taskId);
        return Result.success(task);
    }

    @PostMapping
    public Result add(@RequestBody TaskDto taskDto){
        log.info("管理员发布任务");
        tasksService.add(taskDto);
        return Result.success();
    }

    @PostMapping("/publish/apply")
    public Result nadd(@RequestBody TaskDto taskDto){
        log.info("普通用户发布任务");
        tasksService.nadd(new TaskDto());
        return Result.success();
    }

    @PostMapping("/apply/{taskId}")
    public Result apply(@PathVariable Long taskId){
        log.info("用户申请完成任务");
        tasksService.apply(taskId);
        return Result.success();
    }

    @GetMapping("/my-applies")
    public Result myApplies(){
        log.info("获取完成任务申请列表");
        List<TaskSubmitVo> list = tasksService.myApplies();
        return Result.success(list);
    }
}
