package com.example.shopapp.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.enums.SqlLike;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.shopapp.Util.ResponseCode;
import com.example.shopapp.Util.ServerResponse;
import com.example.shopapp.entity.BaseModel;
import com.example.shopapp.entity.GoodsList;
import com.example.shopapp.dao.GoodsMapper;
import com.example.shopapp.service.IGoodsService;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class GoodsServiceimpl implements IGoodsService {

    @Autowired
    GoodsMapper goodsMapper;
    @Override
    public List<GoodsList> getGoodsList() {

        return goodsMapper.getGoodsList();
    }

//    public List<GoodsList> getGoodsList(int pageNum,int pageSize){
//        IPage<GoodsList> goodsListIPage=new Page<>(pageNum,pageSize);
//        IPage<GoodsList> goodsPage=goodsMapper.selectPage(goodsListIPage,null);
//        return goodsPage.getRecords();
//    }

    @Override
    public List<GoodsList> SelectByCategoryId(int categoryId) {
        return goodsMapper.SelectByCategoryId(categoryId);
    }

    @Override
    public IPage<GoodsList> SearchGoods(Integer pageNum,Integer pageSize, String keyword) {

        QueryWrapper<GoodsList> wrapper=new QueryWrapper<>();

        wrapper.like("text",keyword);


        IPage<GoodsList> pages=new Page<>(pageNum,pageSize);
        IPage<GoodsList> goodsListIPage=goodsMapper.selectPage(pages,wrapper);



            return goodsListIPage;
//        return goodsMapper.SearchGoods(keyword);
    }

    @Override
    public GoodsList SelectByGoodsId(int goodsId) {
        return goodsMapper.SelectByGoodsId(goodsId);
    }

    @Override
    public ServerResponse updateGoodsStock(Integer goodsId, Integer stock) {
        int i=this.updateStock(goodsId,stock);
        if (i==0){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.PRODUCT_STOCK_UPDATE_FAIL.getCode(), ResponseCode.ORDERITEM_INSERT_FAIL.getMsg());

        }
        return ServerResponse.createBySuccess();
    }

    private int updateStock(Integer goodsId,Integer stock){
        GoodsList goodsList=new GoodsList();
        UpdateWrapper<GoodsList> wrapper=new UpdateWrapper<>();
        wrapper.eq("goods_id",goodsId);
        goodsList.setStock(stock);
        goodsList.setUpdateTime(new Date());
        return goodsMapper.update(goodsList,wrapper);
    }



}
