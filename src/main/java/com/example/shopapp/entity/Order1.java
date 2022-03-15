package com.example.shopapp.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Date;

@Data

public class Order1 {
@TableId(type = IdType.AUTO)
    private Integer id;
    private Integer userId;
    private Long orderNo;
    private Integer shippingId;
    private BigDecimal payment;
    private Integer paymentType;
    private Integer postage;
    private Integer status;
@TableField( insertStrategy = FieldStrategy.IGNORED)
    private Date paymentTime;
    @TableField( insertStrategy = FieldStrategy.IGNORED)
    private Date sendTime;
    @TableField( insertStrategy = FieldStrategy.IGNORED)
    private Date endTime;
    @TableField( insertStrategy = FieldStrategy.IGNORED)
    private Date closeTime;
    private Date createTime;
    private Date updateTime;
}
