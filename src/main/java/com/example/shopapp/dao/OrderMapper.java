package com.example.shopapp.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.shopapp.entity.Order1;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface OrderMapper extends BaseMapper<Order1> {
    Order1 findOrderByOrderNo(@Param("orderNo") Long orderNo);
    @Select("select * from order1 where user_id=#{userId} and status=#{status}")
    List<Order1> selectByUserIdAndStatus( Integer userId, Integer status);
//    int insert(Order record);

}
