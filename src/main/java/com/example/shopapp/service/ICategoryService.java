package com.example.shopapp.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.shopapp.entity.Category;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 梁艺荣
 * @since 2021-11-21
 */
public interface ICategoryService  {
    List<Category> getCategoryList(Category category);


}
