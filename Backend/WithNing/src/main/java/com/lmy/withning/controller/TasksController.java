package com.lmy.withning.controller;

import com.lmy.withning.common.Result.Result;
import com.lmy.withning.pojo.entity.Task;
import com.lmy.withning.service.TasksService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
@Slf4j
public class TasksController {

    @Autowired
    private TasksService tasksService;

    @GetMapping("/daily")
    public Result daily(){
        List<Task> list = tasksService.daily();
        log.info("list: {}", list);
        return Result.success(list);
    }
}
