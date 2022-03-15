package com.example.shopapp.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
@Component
public class GoodsList {
  @TableId(value = "goods_id")
   private   Integer goodsId;
     private   String text;
     private String image;
     private Integer spansize;
     private  Integer categoryId;
     private BigDecimal price;
     private Integer status;
     private  Integer stock;
    private String banner;
    private  String detail;
  private Date createTime;
  private Date updateTime;

}
