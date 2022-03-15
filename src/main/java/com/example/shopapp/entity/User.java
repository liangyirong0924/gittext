package com.example.shopapp.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.Date;

@Data
@Component
public class User {
@TableId(value = "id")
    Integer id;

    String username;
    String password;
    String avatar;
    String phonenumber;
     String idcard;
     String question;
     String answer;
    private Date createTime;
    private Date updateTime;
}
