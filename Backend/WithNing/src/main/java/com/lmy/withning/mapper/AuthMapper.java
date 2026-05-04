package com.lmy.withning.mapper;

import com.lmy.withning.pojo.dto.UserDto;
import com.lmy.withning.pojo.entity.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface AuthMapper {
    @Select("select * from user where username=#{username} and password=#{password}")
    User login(UserDto userDto);
    
    @Select("select * from user where username=#{username}")
    User findByUsername(String username);
    
    @Insert("insert into user(username, password, nickname, email, role, points, create_time, update_time) values(#{username}, #{password}, #{nickname}, #{email}, '2', 0, now(), now())")
    void register(User user);
    
    @Select("select * from user where id=#{id}")
    User findById(Integer id);
}
