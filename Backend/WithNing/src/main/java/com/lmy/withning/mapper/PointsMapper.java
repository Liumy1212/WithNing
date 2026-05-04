package com.lmy.withning.mapper;

import com.lmy.withning.pojo.entity.PointRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface PointsMapper {
    @Select("SELECT * FROM point_record WHERE user_id = #{userId} ORDER BY create_time DESC")
    List<PointRecord> getPointRecords(Integer userId);
}
