package com.example.shopapp.entity;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Data
@Component
public class CartVo {
    private List<CartGoodsListVo> cartGoodsListVos;
    private BigDecimal cartTotalPrice;
    private boolean allChecked;
    private String image;
}
