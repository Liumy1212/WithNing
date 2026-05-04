package com.lmy.withning.service;

import com.lmy.withning.pojo.entity.User;
import com.lmy.withning.pojo.vo.TaskCompleteApplyVo;
import com.lmy.withning.pojo.vo.TaskPublishApplyVo;

import java.util.List;

public interface AdminService {
    List<TaskPublishApplyVo> getTaskPublishApplies();
    void reviewTaskPublish(Long taskId, Integer status);
    List<TaskCompleteApplyVo> getTaskCompleteApplies();
    void reviewTaskComplete(Long applyId, Integer status);
    void completeTaskDirectly(Long taskId);
    List<User> getAllUsers();
}
