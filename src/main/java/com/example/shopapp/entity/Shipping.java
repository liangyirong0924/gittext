package com.example.shopapp.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Date;

@Data
@Component
public class Shipping implements Serializable {
    @TableField(value = "shipping_id")
    @TableId(type = IdType.AUTO)
    private Integer id;
    private Integer userId;
    private  String receiverName;
    private  String receiverPhone;
    private  String receiverProvince;
    private  String receiverCity;
    private  String receiverDistrict;
    private  String receiverAddress;


    private String receiverZip;
    private Date createTime;
    private Date updateTime;
    private int isDefault;
}
