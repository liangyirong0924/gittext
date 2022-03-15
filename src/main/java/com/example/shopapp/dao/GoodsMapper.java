package com.example.shopapp.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.shopapp.entity.GoodsList;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface GoodsMapper  extends BaseMapper<GoodsList> {
    @Select("select * from goods_list")
    List<GoodsList> getGoodsList();
@Select("select  goods_list.goods_id ,goods_list.text,goods_list.image,goods_list.price from goods_list,category where category.id=goods_list.category_id and goods_list.category_id=${category_id}")
    List<GoodsList> SelectByCategoryId(int categoryId);


List<GoodsList> SearchGoods(IPage<GoodsList> page,
                                        @Param("keyword") String keyword);
@Select("select * from goods_list where goods_list.goods_id=${goods_id} ")
GoodsList SelectByGoodsId(int goodsId);

//int updateGoodsStock(@Param("goodsId") Integer goodsId,@Param("stock")Integer stock);
}
