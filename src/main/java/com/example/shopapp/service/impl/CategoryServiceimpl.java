package com.example.shopapp.service.impl;

import com.example.shopapp.entity.Category;
import com.example.shopapp.dao.CategoryMapper;
import com.example.shopapp.service.ICategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 梁艺荣
 * @since 2021-11-21
 */
@Service
public class CategoryServiceimpl implements ICategoryService {
    @Autowired
    CategoryMapper categoryMapper;

    @Override
    public List<Category> getCategoryList(Category category) {
        return categoryMapper.getCategoryList(category);
    }
}
