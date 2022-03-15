package com.example.shopapp.entity;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Data
@Component
public class CartGoodsListVo {
    private  Integer cartId;
    private  Integer userId;
    private  Integer goodsId;
    private String goodsText;
    private String goodsImage;
    private Integer quantity;
    private BigDecimal goodsPrice;
    private BigDecimal goodsTotalPrice;
    private Integer goodsStock;
    private Integer goodsChecked;
    private Integer goodsStatus;
    private String limitQuantity;
}
