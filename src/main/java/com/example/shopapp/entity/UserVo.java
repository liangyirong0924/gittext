package com.example.shopapp.entity;

import lombok.Data;

import java.util.Date;

@Data
public class UserVo {
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
