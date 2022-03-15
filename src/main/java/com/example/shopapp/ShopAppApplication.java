package com.example.shopapp;


import org.mybatis.spring.annotation.MapperScan;

import org.springframework.boot.SpringApplication;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;
@ComponentScans(value = {@ComponentScan("com.example.shopapp.controller"),
//@ComponentScan("com.example.shopapp.dao.impl"),@ComponentScan("com.example.shopapp.dao"),
@ComponentScan("com.example.shopapp.entity"),@ComponentScan("com.example.shopapp.service.impl"),
@ComponentScan("com.example.shopapp.service")})
@MapperScan("com.example.shopapp.dao")
@SpringBootApplication
public class ShopAppApplication {

    public static void main(String[] args) {

        SpringApplication.run(ShopAppApplication.class, args);
        System.out.println("hello");
    }


}
