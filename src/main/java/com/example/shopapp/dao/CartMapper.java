package com.example.shopapp.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.shopapp.entity.Cart;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface CartMapper extends BaseMapper<Cart> {
    int deleteByUserIdGoodsIds(@Param("userId") Integer userId,@Param("goodsList")List<String> goodsList);

    int selectCartProductCount(@Param("userId") Integer userId);
    @Select("select checked from cart where goods_id=${goodsId} and user_id=${userId}")
    int  findCheckByGoodsId(@Param("goodsId")Integer goodsId,@Param("userId") Integer userId);
    int  unCheckedCount(@Param("userId")Integer userId);

}
