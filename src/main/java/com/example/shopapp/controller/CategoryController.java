package com.example.shopapp.controller;


import com.example.shopapp.entity.BaseModel;
import com.example.shopapp.entity.Category;
import com.example.shopapp.entity.GoodsList;
import com.example.shopapp.service.ICategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 梁艺荣
 * @since 2021-11-21
 */
@Controller
@RequestMapping("/category")
public class CategoryController {
    @Autowired
    ICategoryService categoryService;
    @RequestMapping("list")
    @ResponseBody
    public BaseModel getCategoryAll(Category category) {
        List<Category> categories=categoryService.getCategoryList(category);
        BaseModel model;
        if (categories == null) {
            model = BaseModel.createRespose(201, "获取数据失败");

        } else {
            model = BaseModel.createRespose(200, "获取数据成功");

            model.setDatas(categories);
        }
        return model;
    }
}
