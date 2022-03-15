package com.example.shopapp.controller;

import com.example.shopapp.Util.*;
import com.example.shopapp.entity.Cart;
import com.example.shopapp.entity.GoodsList;
import com.example.shopapp.entity.OrderItem;
import com.example.shopapp.entity.UserVo;
import com.example.shopapp.service.ICart1Service;
import com.example.shopapp.service.IGoodsService;
import com.example.shopapp.service.IOrder1Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/order")
public class OrderController {
    @Autowired
    IOrder1Service order1Service;



    @ResponseBody
    @RequestMapping("create")
       public ServerResponse createOrder(HttpSession session,Integer shippingId){
        UserVo userVO=(UserVo) session.getAttribute(Const.LOGIN_SUCCESS);
      return order1Service.createOrderByCart(userVO.getId(),shippingId);
    }
    //直接创建订单
    @ResponseBody
    @RequestMapping("create_order_direct")
    public ServerResponse createOrderDirect(HttpSession session,Integer goodsId,Integer quantity, Integer shippingId){
        UserVo userVO=(UserVo) session.getAttribute(Const.LOGIN_SUCCESS);
        return order1Service.createOrderDirect(userVO.getId(),goodsId,quantity,shippingId);
    }

    @ResponseBody
    @RequestMapping("cancel")
    public ServerResponse cancel(HttpSession session,Long orderNo){
        UserVo userVO=(UserVo) session.getAttribute(Const.LOGIN_SUCCESS);

        return order1Service.cancel(userVO.getId(), orderNo);
    }
    @RequestMapping("get_order_cart_product")
    @ResponseBody
    public ServerResponse getOrderCartGoods(HttpSession session){
        UserVo userVO=(UserVo) session.getAttribute(Const.LOGIN_SUCCESS);
        return order1Service.getOrderCartGoods(userVO.getId());
    }
    @RequestMapping("detail")
    @ResponseBody
    public ServerResponse detail(HttpSession session, Long orderNo){

        UserVo userVO=(UserVo) session.getAttribute(Const.LOGIN_SUCCESS);
        return order1Service.getOrderDetail(userVO.getId(),orderNo);
    }
    @RequestMapping("list")
    @ResponseBody
    public ServerResponse OrderList(HttpSession session, Integer status ,
                                    @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                     @RequestParam(value = "pageSize",defaultValue = "10") int pageSize){
        UserVo userVO=(UserVo) session.getAttribute(Const.LOGIN_SUCCESS);

        return order1Service.getOrderList(userVO.getId(),status,pageNum,pageSize);
    }
    @RequestMapping("confirm_received_goods.do")
    @ResponseBody
    public ServerResponse<Boolean> confirmReceivedGoods(HttpSession session, Long orderNo){
        UserVo userVO=(UserVo) session.getAttribute(Const.LOGIN_SUCCESS);
        ServerResponse serverResponse = order1Service.confirmReceivedGoods(userVO.getId(),orderNo);
        if(serverResponse.isSuccess()){
            return ServerResponse.createBySuccessMessage(serverResponse.getMsg());
        }
        return ServerResponse.createByErrorMessage(serverResponse.getMsg());
    }
}
