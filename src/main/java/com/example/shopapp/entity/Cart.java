package com.example.shopapp.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class Cart  implements Serializable {
    @TableId(value = "cart_id" ,type = IdType.AUTO)
    private int cartId;
    private  Integer userId;
    private Integer goodsId;
    private Integer quantity;
    private Integer checked;
    private Date createTime;
    private Date updateTime;
}
