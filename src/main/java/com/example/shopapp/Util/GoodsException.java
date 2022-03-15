package com.example.shopapp.Util;

public class GoodsException extends RuntimeException{

    private String msg;
    private int id;

    public  GoodsException(String msg){
        super(msg);
        this.id=id;
        this.msg=msg;
    }


}
