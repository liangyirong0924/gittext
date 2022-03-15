package com.example.shopapp.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.shopapp.entity.OrderItem;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface OrderItemMapper extends BaseMapper<OrderItem> {

    int insertBatch(@Param("orderItemList")List<OrderItem> record);

}
