package com.example.shopapp.dao;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.shopapp.entity.Category;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author 梁艺荣
 * @since 2021-11-21
 */
@Mapper
public interface CategoryMapper extends BaseMapper<Category> {
    @Select("select * from category")
    List<Category> getCategoryList(Category category);
}
