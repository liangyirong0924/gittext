package com.example.shopapp.Util;

/**
 * Created by geely
 */
public enum ResponseCode {

    SUCCESS(200,"成功"),
    ERROR(201,"失败"),
    ON_SALE(1,"在线"),
    USERNAME_NOT_EMPTY(10,"用户名不能为空"),
    PASSWORD_NOT_EMPTY(11,"密码不能为空"),
    USERNAME_NOT_EXISTS(12,"用户名不存在"),
    ALTER_ERROR(31,"身份证或密保问题答案错误"),
    PASSWORD_ERROR(13,"密码错误"),
    PASSWORD_EXISTS(32,"密码不能重复"),
    EMAIL_NOT_EMPTY(6,"身份证不能为空"),
    PHONE_NOT_EMPTY(7,"联系方式不能为空"),
    QUESTION_NOT_EMPTY(8,"密保问题不能为空"),
    ANSWER_NOT_EMPTY(9,"密保答案不能为空"),
    USERNAME_EXISTS(15,"用户名存在"),
    EMAIL_EXISTS(16,"邮箱存在"),
    REGISTER_FAIL(17,"注册失败"),
    USERINFO_UPDATE_FAIL(18,"用户信息修改失败"),
    NEED_LOGIN(10,"没有登录！"),
    PARAMTER_NOT_EMPTY(5,"参数不能为空"),
    ILLEGAL_ARGUMENT(2,"ILLEGAL_ARGUMENT"),
    ORDERITEM_INSERT_FAIL(20,"订单明细插入失败"),
    ORDER_CREATE_FAIL(21,"订单创建失败"),
    ILLEGAL_PARAM(15,"非法参数"),
    CART_UPDATE_FAIL(16,"购物车商品更新失败"),
    CART_ADD_FAIL(17,"购物车商品添加失败"),
    PRODUCT_NOT_EXISTS(18,"商品不存在"),
    CART_NOT_SELECTED(19,"购物车为空或者没有被选中的商品"),
    PRODUCT_STOCK_UPDATE_FAIL(22,"商品库存更新失败"),
    PAY_PARAM_ERROR(25,"支付参数有误"),
    ORDER_NO_NOT_EMPTY(26,"订单编号不能为空"),
    ORDER_NOT_EXISTS(27,"订单不存在"),
    ORDER_UPDATE_FAIL(28,"订单更新失败"),
    NLINE_PAY(1,"在线支付");

    private final int code;
    private final String msg;


    ResponseCode(int code, String msg){
        this.code = code;
        this.msg = msg;
    }

    public int getCode(){
        return code;
    }
    public String getMsg(){
        return msg;
    }

}
