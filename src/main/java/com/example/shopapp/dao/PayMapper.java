package com.example.shopapp.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.shopapp.entity.PayInfo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PayMapper extends BaseMapper<PayInfo> {
}
