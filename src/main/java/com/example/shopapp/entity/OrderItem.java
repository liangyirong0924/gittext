package com.example.shopapp.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class OrderItem {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private Integer userId;
    private Long orderNo;
    private Integer goodsId;
    private  String goodsText;
    private  String goodsImage;
    private BigDecimal currentUnitPrice;
    private Integer quantity;
    private  BigDecimal totalPrice;
    private Date createTime;
    private Date updateTime;
}
