package com.lmy.withning.service.impl;

import com.lmy.withning.mapper.TasksMapper;
import com.lmy.withning.pojo.entity.Task;
import com.lmy.withning.service.TasksService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TasksServiceImpl implements TasksService {

    @Autowired
    private TasksMapper tasksMapper;

    @Override
    public List<Task> daily() {
        return tasksMapper.daily();
    }
}
