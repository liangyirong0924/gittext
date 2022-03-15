package com.example.shopapp.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.shopapp.Util.ServerResponse;
import com.example.shopapp.entity.OrderVo;
import com.github.pagehelper.PageInfo;

import java.util.Date;

public interface IOrder1Service {
    ServerResponse getOrderCartGoods(Integer userId);
    ServerResponse createOrderByCart(Integer userId, Integer shippingId);
    ServerResponse<String> cancel(Integer userId, Long orderNo);
    ServerResponse<OrderVo> getOrderDetail(Integer userId, Long orderNo);
    public ServerResponse createOrderDirect(Integer userId,Integer goodsId,Integer quantity, Integer shippingId);
   public   ServerResponse<PageInfo> getOrderList(Integer userId, Integer status,int pageNum,int pageSize);
    ServerResponse confirmReceivedGoods(Integer userId, Long orderNo);
    public ServerResponse findOrderByOrderno(Long orderNo);
    public ServerResponse updateOrderPayStatus(Long orderNo,Integer status, Date payTime);
}
