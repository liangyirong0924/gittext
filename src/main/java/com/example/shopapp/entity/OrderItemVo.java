package com.example.shopapp.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderItemVo {
    @TableId(value = "order_no")
    private Long orderNo;
    private Integer goodsId;
    private String goodsText;
    private String goodsImage;
    private BigDecimal currentUnitPrice;
    private Integer quantity;
    private BigDecimal totalPrice;
    private String createTime;

}
