package com.example.shopapp.Util;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
import java.util.List;

/**
 * Created by geely
 */
//@JsonSerialize(include =  JsonSerialize.Inclusion.NON_NULL)
//保证序列化json的时候,如果是null的对象,key也会消失


public class ServerResponse<T> implements Serializable {

    private int status;
    private String msg;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private T data;



    private ServerResponse(int status){
        this.status = status;
    }



    private ServerResponse(int status, T data){
        this.status = status;
        this.data = data;
    }


    private ServerResponse(int status, String msg){
        this.status = status;
        this.msg = msg;
    }
    @JsonIgnore
    public  boolean isSucess(){
        return this.status==200;
    }

    @JsonIgnore
    public boolean isSucesss1(){return this.status==0;}

    private ServerResponse(int status, T data,String msg){
        this.status = status;
        this.msg = msg;
        this.data = data;
    }



    @JsonIgnore
    //使之不在json序列化结果当中
    public boolean isSuccess(){
        return this.status == ResponseCode.SUCCESS.getCode();
    }

    public int getStatus(){
        return status;
    }
    public T getData(){
        return data;
    }
    public String getMsg(){
        return msg;
    }

    public  static ServerResponse  createServerResponseBySucess(){
        return new ServerResponse(0);
    }

    public static <T> ServerResponse<T> createBySuccess(){
        return new ServerResponse<T>(ResponseCode.SUCCESS.getCode());
    }

    public static <T> ServerResponse<T> createBySuccessMessage(String msg){
        return new ServerResponse<T>(ResponseCode.SUCCESS.getCode(),msg);
    }

    public static <T> ServerResponse<T> createBySuccess(T data){
        return new ServerResponse<T>(ResponseCode.SUCCESS.getCode(),data);
    }


    public static <T> ServerResponse<T> createBySuccess(String msg, T data){
        return new ServerResponse<T>(ResponseCode.SUCCESS.getCode(),data,msg);
    }


    public static <T> ServerResponse<T> createByError(int code, String msg){
        return new ServerResponse<T>(ResponseCode.ERROR.getCode(), ResponseCode.ERROR.getMsg());
    }


    public static <T> ServerResponse<T> createByErrorMessage(String errorMessage){
        return new ServerResponse<T>(ResponseCode.ERROR.getCode(),errorMessage);
    }


    public static <T> ServerResponse<T> createByErrorCodeMessage(int errorCode, String errorMessage){
        return new ServerResponse<T>(errorCode,errorMessage);
    }
    public  static ServerResponse  createServerResponseByFail(int status,String msg){
        return new ServerResponse(status,null,msg);
    }












}
