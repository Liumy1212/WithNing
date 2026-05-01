package com.lmy.withning.mapper;

import com.lmy.withning.pojo.dto.UserDto;
import com.lmy.withning.pojo.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface AuthMapper {
    @Select("select * from user where username=#{username} and password=#{password}")
    User login(UserDto userDto);
}
