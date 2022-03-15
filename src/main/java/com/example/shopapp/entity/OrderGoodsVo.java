package com.example.shopapp.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class OrderGoodsVo {
    private List<OrderItemVo> orderItemVoList;
    private BigDecimal goodsTotalPrice;
    private String image;
}
