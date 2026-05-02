package com.lmy.withning.service.impl;

import com.lmy.withning.mapper.TasksMapper;
import com.lmy.withning.pojo.dto.TaskDto;
import com.lmy.withning.pojo.entity.Task;
import com.lmy.withning.pojo.entity.TaskSubmit;
import com.lmy.withning.pojo.vo.TaskSubmitVo;
import com.lmy.withning.service.TasksService;
import com.lmy.withning.utils.UserContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class TasksServiceImpl implements TasksService {

    @Autowired
    private TasksMapper tasksMapper;

    @Override
    public List<Task> daily(Long userId) {
        return tasksMapper.daily(userId);
    }

    @Override
    public List<Task> stage(Long userId) {
        return tasksMapper.stage(userId);
    }

    @Override
    public void add(TaskDto taskDto) {
        log.info("管理员添加任务");
        Task task = new Task();
        BeanUtils.copyProperties(taskDto, task);
        task.setUserId(Long.valueOf(UserContextHolder.getUser().getId()));
        task.setStatus(1);
        task.setCreateTime(LocalDateTime.now());
        tasksMapper.add(task);
    }

    @Override
    public Task getById(Long taskId) {
        return tasksMapper.getById(taskId);
    }

    @Override
    public void nadd(TaskDto taskDto) {
        log.info("普通用户添加任务");
        Task task = new Task();
        BeanUtils.copyProperties(taskDto, task);
        task.setUserId(Long.valueOf(UserContextHolder.getUser().getId()));
        task.setStatus(2);
        task.setCreateTime(LocalDateTime.now());
        tasksMapper.add(task);
    }

    @Override
    public void apply(Long taskId) {
        log.info("用户申请完成任务");
        Task task = tasksMapper.getById(taskId);
        TaskSubmit taskSubmit = new TaskSubmit();
        BeanUtils.copyProperties(task, taskSubmit);
        taskSubmit.setUserId(UserContextHolder.getUser().getId());
        taskSubmit.setTaskId(taskId.intValue());  // 添加这行
        taskSubmit.setStatus(2);
        taskSubmit.setCreateTime(LocalDateTime.now());
        taskSubmit.setUpdateTime(LocalDateTime.now());
        tasksMapper.apply(taskSubmit);
    }

    @Override
    public List<TaskSubmitVo> myApplies() {
        Integer userId = UserContextHolder.getUser().getId();
        List<TaskSubmit> list1 = tasksMapper.myApplies(userId);
        List<TaskSubmitVo> list2 = new ArrayList<>();
        for (TaskSubmit taskSubmit : list1) {
            TaskSubmitVo taskSubmitVo = new TaskSubmitVo();
            BeanUtils.copyProperties(taskSubmit, taskSubmitVo);
            list2.add(taskSubmitVo);
        }
        return list2;
    }

}
