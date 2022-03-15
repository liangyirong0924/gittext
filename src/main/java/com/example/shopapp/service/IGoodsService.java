package com.example.shopapp.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.shopapp.Util.ServerResponse;
import com.example.shopapp.entity.GoodsList;

import java.util.List;

public interface IGoodsService {
    List<GoodsList> getGoodsList();
    List<GoodsList> SelectByCategoryId(int categoryId);
    IPage<GoodsList>  SearchGoods(Integer pageNum,Integer pageSize, String keyword);
    GoodsList  SelectByGoodsId(int goodsId);
//  ServerResponse SearchGoods(Integer categoryId, String keyword,Integer pageNum,Integer pageSize);
              public ServerResponse updateGoodsStock(Integer goodsId, Integer stock);
}
