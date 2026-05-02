package com.lmy.withning.service;

import com.lmy.withning.pojo.dto.TaskDto;
import com.lmy.withning.pojo.entity.Task;
import com.lmy.withning.pojo.vo.TaskSubmitVo;

import java.util.List;

public interface TasksService {

    List<Task> daily(Long userId);

    List<Task> stage(Long userId);

    void add(TaskDto taskDto);

    Task getById(Long taskId);

    void nadd(TaskDto taskDto);

    void apply(Long taskId);

    List<TaskSubmitVo> myApplies();
}
