package com.lmy.withning.service.impl;

import com.lmy.withning.mapper.AdminMapper;
import com.lmy.withning.mapper.TasksMapper;
import com.lmy.withning.pojo.entity.PointRecord;
import com.lmy.withning.pojo.entity.Task;
import com.lmy.withning.pojo.entity.TaskSubmit;
import com.lmy.withning.pojo.entity.User;
import com.lmy.withning.pojo.entity.UserTask;
import com.lmy.withning.pojo.vo.TaskCompleteApplyVo;
import com.lmy.withning.pojo.vo.TaskPublishApplyVo;
import com.lmy.withning.service.AdminService;
import com.lmy.withning.utils.UserContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class AdminServiceImpl implements AdminService {

    @Autowired
    private AdminMapper adminMapper;

    @Autowired
    private TasksMapper tasksMapper;

    @Override
    public List<TaskPublishApplyVo> getTaskPublishApplies() {
        log.info("获取任务发布申请列表");
        return adminMapper.getTaskPublishApplies();
    }

    @Override
    public void reviewTaskPublish(Long taskId, Integer status) {
        log.info("审核任务发布申请，taskId: {}, status: {}", taskId, status);
        adminMapper.reviewTaskPublish(taskId, status);
    }

    @Override
    public List<TaskCompleteApplyVo> getTaskCompleteApplies() {
        log.info("获取任务完成申请列表");
        return adminMapper.getTaskCompleteApplies();
    }

    @Override
    public void reviewTaskComplete(Long applyId, Integer status) {
        log.info("审核任务完成申请，applyId: {}, status: {}", applyId, status);
        Integer reviewerId = UserContextHolder.getUser().getId();
        adminMapper.reviewTaskComplete(applyId, status, reviewerId);
        
        if (status == 1) {
            TaskSubmit taskSubmit = adminMapper.getTaskSubmitById(applyId);
            if (taskSubmit != null) {
                adminMapper.addUserPoints(taskSubmit.getUserId(), taskSubmit.getRewardPoints());
                
                PointRecord pointRecord = new PointRecord();
                pointRecord.setUserId(taskSubmit.getUserId());
                pointRecord.setPoints(taskSubmit.getRewardPoints());
                pointRecord.setDescription("完成任务：" + taskSubmit.getTitle());
                pointRecord.setType(1);
                adminMapper.addPointRecord(pointRecord);
                
                UserTask userTask = new UserTask();
                userTask.setUserId(taskSubmit.getUserId());
                userTask.setTaskId(taskSubmit.getTaskId());
                adminMapper.addUserTask(userTask);
            }
        }
    }

    @Override
    public void completeTaskDirectly(Long taskId) {
        log.info("管理员直接完成任务，taskId: {}", taskId);
        Task task = tasksMapper.getById(taskId);
        if (task != null) {
            Integer adminId = UserContextHolder.getUser().getId();
            adminMapper.addUserPoints(adminId, task.getRewardPoints());
            
            PointRecord pointRecord = new PointRecord();
            pointRecord.setUserId(adminId);
            pointRecord.setPoints(task.getRewardPoints());
            pointRecord.setDescription("完成任务：" + task.getTitle());
            pointRecord.setType(1);
            adminMapper.addPointRecord(pointRecord);
            
            UserTask userTask = new UserTask();
            userTask.setUserId(adminId);
            userTask.setTaskId(taskId.intValue());
            adminMapper.addUserTask(userTask);
        }
    }

    @Override
    public List<User> getAllUsers() {
        log.info("获取所有用户列表");
        return adminMapper.getAllUsers();
    }
}

