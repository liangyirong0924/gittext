package com.example.shopapp.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.example.shopapp.Util.*;
import com.example.shopapp.dao.PayMapper;
import com.example.shopapp.entity.Order1;
//import com.example.shopapp.service.IOrderService;
import com.example.shopapp.entity.PayInfo;
import com.example.shopapp.service.IOrder1Service;
import com.example.shopapp.service.IPayService;
import freemarker.template.utility.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;

@Service
public class PayServicelmpl implements IPayService {
    @Autowired
    IOrder1Service orderService;
    @Autowired
    PayMapper payMapper;

    @Value("${appid}")
    private String APPID;
    @Value("${rsa2_private}")
    private String RSA2_PRIVATE;
    @Value("${notify_url}")
    private String NOTIFY_URL;


    @Override
    public ServerResponse pay(Long orderNo) {
        if (StringUtils.isBlank(APPID) || (StringUtils.isBlank(RSA2_PRIVATE))) {

            return ServerResponse.createByErrorCodeMessage(ResponseCode.PAY_PARAM_ERROR.getCode(), ResponseCode.PAY_PARAM_ERROR.getMsg());
        }
        ServerResponse serverResponse = orderService.findOrderByOrderno(orderNo);
        if (!serverResponse.isSuccess()) {
            return serverResponse;
        }
        Order1 order1 = (Order1) serverResponse.getData();
        Map<String, String> params = OrderInfoUtil2_0.buildOrderParamMap(APPID, true, order1, NOTIFY_URL);
        String orderParam = OrderInfoUtil2_0.buildOrderParam(params);
        String sign = OrderInfoUtil2_0.getSign(params, RSA2_PRIVATE, true);
        final String orderInfo = orderParam + "&" + sign;


        return ServerResponse.createBySuccess(orderInfo);
    }

    @Override
    public String callbackLogin(Map<String, String> singMap) {

        //获取订单编号
        Long orderNo = Long.valueOf(singMap.get("out_trade_no"));

        //检查orderno是否存在
        ServerResponse serverResponse = orderService.findOrderByOrderno(orderNo);
        if (!serverResponse.isSuccess()) {
            return "success";
        }
        //判断订单是否被修改
        Order1 order1 = (Order1) serverResponse.getData();

        //修改订单状态
        if (order1.getStatus() >= OrderStatusEnum.ORDER_SEND.getStatus()) {
            //订单已经支付
            return "success";
        }
        //获取订单的支付状态
        String trade_status = (String) singMap.get("trade_status");
        Date pay_time = DateTimeUtil.strToDate((String) singMap.get("gmt_payment"));

        ServerResponse serverResponse1 = orderService.updateOrderPayStatus(orderNo, TradeStatusEnum.statusof(trade_status), pay_time);
        if (!serverResponse1.isSuccess()) {
            return "fail";
        }
            //插入支付信息
            PayInfo payInfo = new PayInfo();
            payInfo.setOrderNo(order1.getOrderNo());
            payInfo.setUserId(order1.getUserId());
            payInfo.setPayPlatform(1);
            payInfo.setPlatformNumber(singMap.get("trade_no"));
            payInfo.setPlatformStatus(trade_status);
            payInfo.setCreateTime(new Date());
            payInfo.setUpdateTime(new Date());
            //返回结果
          PayInfo payInfo1=findPayInfoByOrderNo(orderNo);
        int count;
          if (payInfo1==null){
              //插入
         count =   payMapper.insert(payInfo);
          }else {
              //更新
              payInfo.setId(payInfo1.getId());
           count=   payMapper.updateById(payInfo);
          }
        if (count==0){
            return "fail";
        }
            return "success";



    }
    //根据订单号查询支付信息
    private PayInfo findPayInfoByOrderNo(Long orderNo){
        QueryWrapper queryWrapper=new QueryWrapper();
        queryWrapper.eq("order_no",orderNo);
        PayInfo payInfo=payMapper.selectOne(queryWrapper);
        return payInfo;
    }
}
