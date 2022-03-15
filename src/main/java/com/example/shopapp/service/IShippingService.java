package com.example.shopapp.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.shopapp.Util.ServerResponse;
import com.example.shopapp.entity.Shipping;
import com.github.pagehelper.PageInfo;

public interface IShippingService {
    ServerResponse addShipping(Integer userId, Shipping shipping);
    ServerResponse<String> delShipping(Integer userId, Integer shippingId);
    ServerResponse updateShipping(Integer userId, Shipping shipping);
    ServerResponse<Shipping> selectShipping(Integer userId, Integer shippingId);
    ServerResponse<PageInfo> shippingList(Integer userId, Integer pageNum, Integer pageSize);
    ServerResponse updateUserDefault(Integer userId,Integer shippingId);
}
