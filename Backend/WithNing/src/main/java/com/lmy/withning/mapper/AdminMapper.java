package com.lmy.withning.mapper;

import com.lmy.withning.pojo.entity.PointRecord;
import com.lmy.withning.pojo.entity.TaskSubmit;
import com.lmy.withning.pojo.entity.User;
import com.lmy.withning.pojo.entity.UserTask;
import com.lmy.withning.pojo.vo.TaskCompleteApplyVo;
import com.lmy.withning.pojo.vo.TaskPublishApplyVo;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface AdminMapper {
    @Select("SELECT t.*, u.nickname as userNickname, u.id as userId FROM task t JOIN user u ON t.publisher_id = u.id WHERE t.status = 2")
    List<TaskPublishApplyVo> getTaskPublishApplies();
    
    @Update("UPDATE task SET status = #{status} WHERE id = #{taskId}")
    void reviewTaskPublish(@Param("taskId") Long taskId, @Param("status") Integer status);
    
    @Select("SELECT ts.*, u.nickname as userNickname FROM task_submit ts JOIN user u ON ts.user_id = u.id")
    List<TaskCompleteApplyVo> getTaskCompleteApplies();
    
    @Update("UPDATE task_submit SET status = #{status}, reviewer_id = #{reviewerId}, review_time = NOW() WHERE id = #{applyId}")
    void reviewTaskComplete(@Param("applyId") Long applyId, @Param("status") Integer status, @Param("reviewerId") Integer reviewerId);
    
    @Select("SELECT * FROM task_submit WHERE id = #{applyId}")
    TaskSubmit getTaskSubmitById(Long applyId);
    
    @Update("UPDATE user SET points = points + #{points} WHERE id = #{userId}")
    void addUserPoints(@Param("userId") Integer userId, @Param("points") Integer points);
    
    @Insert("INSERT INTO point_record(user_id, points, description, type, create_time) VALUES(#{userId}, #{points}, #{description}, #{type}, NOW())")
    void addPointRecord(PointRecord pointRecord);
    
    @Insert("INSERT INTO user_task(user_id, task_id, completed, complete_time, create_time) VALUES(#{userId}, #{taskId}, true, NOW(), NOW())")
    void addUserTask(UserTask userTask);
    
    @Select("SELECT * FROM user")
    List<User> getAllUsers();
}
