package com.example.shopapp.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.shopapp.entity.*;
import com.example.shopapp.service.IGoodsService;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/goods")
public class GoodsController {
    @Autowired
    IGoodsService iGoodsService;

    @ResponseBody
    @RequestMapping("goodslist")
    public BaseModel  SelectGoodsList(){
       List<GoodsList> goodsList1 = iGoodsService.getGoodsList();
        BaseModel baseModel;
        if (goodsList1 == null) {
            baseModel = BaseModel.createRespose(201, "获取数据失败");

        } else {
            baseModel = BaseModel.createRespose(200, "获取数据成功");

            baseModel.setDatas(goodsList1);
        }
        return baseModel;
    }

    @ResponseBody
    @RequestMapping("category")
    public BaseModel SelectGoodsByCategoryId(int categoryId){
        List<GoodsList> goodsList2 =iGoodsService.SelectByCategoryId(categoryId);
        BaseModel baseModel;
        if (goodsList2 == null) {
            baseModel = BaseModel.createRespose(201, "获取数据失败");

        } else {
            baseModel = BaseModel.createRespose(200, "获取数据成功");

            baseModel.setDatas(goodsList2);
        }
        return baseModel;
    }
    @ResponseBody
    @RequestMapping("list.do")
  public BaseModel SearchGoods(@RequestParam(value = "pageNum",defaultValue = "1") Integer pageNum,
                               @RequestParam(value = "pageSize",defaultValue = "2") Integer pageSize,
                               @RequestParam("keyword") String keyword){
        IPage<GoodsList>  goodsLists=iGoodsService.SearchGoods(pageNum,pageSize,keyword);

        BaseModel baseModel;
        if (goodsLists == null) { //||keyword.isEmpty()
            baseModel = BaseModel.createRespose(201, "获取数据失败");

        } else {
            baseModel = BaseModel.createRespose(200, "获取数据成功");

            baseModel.setData(goodsLists);
        }
        return baseModel;
  }
    @ResponseBody
    @RequestMapping("goodsId")
    public BaseModel SelectGoodsByGoodsId(int goodsId){
        GoodsList goodsList3 =iGoodsService.SelectByGoodsId(goodsId);
        BaseModel baseModel;
        if (goodsList3 == null) {
            baseModel = BaseModel.createRespose(201, "获取数据失败");

        } else {
            baseModel = BaseModel.createRespose(200, "获取数据成功");

            baseModel.setData(goodsList3);
        }
        return baseModel;
    }
}
