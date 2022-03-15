package com.example.shopapp.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class OrderVo {
    @TableId(value = "order_no")
    private Long orderNo;
    private BigDecimal payment;
    private Integer paymentType;
    private Integer postage;
    private Integer status;
    private String statusDesc;
    private String paymentTime;
    private String paymentTypeDesc;
    private String sendTime;
    private String endTime;
    private String closeTime;
    private String createTime;
    private List<OrderItemVo> orderItemVoList;
    private Integer shippingId;
    private String receiveName;
    private ShippingVo shippingVo;
}
