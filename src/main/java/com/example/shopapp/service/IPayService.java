package com.example.shopapp.service;

import com.example.shopapp.Util.ServerResponse;

import java.util.Map;

public interface IPayService {
    public ServerResponse pay(Long orderNo);
    public String  callbackLogin(Map<String,String> singMap);
}
