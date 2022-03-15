package com.example.shopapp.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.shopapp.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper extends BaseMapper<User> {
    @Select("select  * from user where username=#{username} and password=#{password}")
    User selectUserPsd(String username,String password);
}
