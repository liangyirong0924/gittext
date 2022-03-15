package com.example.shopapp.entity;

import com.example.shopapp.Util.ResponseCode;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;

import java.util.List;

@Data

public class BaseModel<T> {
    int code;
    String msg;

    T data;
    List<T> datas;

    @Override
    public String toString() {
        return "BaseModel{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                ", datas=" + datas +
                '}';
    }

    public static BaseModel createRespose(int code, String msg){
        BaseModel model = new BaseModel();
        model.setCode(code);
        model.setMsg(msg);
        return  model;
    }

}
