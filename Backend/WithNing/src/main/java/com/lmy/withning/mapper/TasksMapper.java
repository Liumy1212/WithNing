package com.lmy.withning.mapper;

import com.lmy.withning.pojo.entity.Task;
import com.lmy.withning.pojo.entity.TaskSubmit;
import com.lmy.withning.pojo.vo.TaskSubmitVo;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface TasksMapper {

    //TODO
    @Select("SELECT t.* FROM task t WHERE t.type = 1 AND t.status = 1 AND NOT EXISTS (SELECT 1 FROM task_submit ts WHERE ts.user_id = #{userId} AND ts.task_id = t.id)")
    List<Task> daily(Long userId);

    @Select("SELECT t.* FROM task t WHERE t.type = 2 AND t.status = 1 AND NOT EXISTS (SELECT 1 FROM task_submit ts WHERE ts.user_id = #{userId} AND ts.task_id = t.id)")
    List<Task> stage(Long userId);

    @Insert("insert into task(title, description, type, reward_points, deadline, image_url, publisher_id, status, create_time, update_time) values(#{title}, #{description}, #{type}, #{rewardPoints}, #{deadline}, #{imageUrl}, #{userId}, #{status}, #{createTime}, #{updateTime})")
    void add(Task task);

    @Select("select * from task where id = #{taskId}")
    Task getById(Long taskId);

    @Insert("insert into task_submit(user_id, title, description, type, reward_points, deadline, status, task_id, create_time, update_time) values(#{userId}, #{title}, #{description}, #{type}, #{rewardPoints}, #{deadline}, #{status}, #{taskId}, #{createTime}, #{updateTime})")
    void apply(TaskSubmit taskSubmit);

    @Select("select * from task_submit where user_id = #{userId}")
    List<TaskSubmit> myApplies(Integer userId);
}
