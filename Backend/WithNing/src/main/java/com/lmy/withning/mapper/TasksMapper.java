package com.lmy.withning.mapper;

import com.lmy.withning.pojo.entity.Task;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface TasksMapper {

    @Select("select * from task where type = 1 and status = 1")
    List<Task> daily();

}
