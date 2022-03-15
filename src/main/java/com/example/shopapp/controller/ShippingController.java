package com.example.shopapp.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.shopapp.Util.Const;
import com.example.shopapp.Util.ResponseCode;
import com.example.shopapp.Util.ServerResponse;
import com.example.shopapp.entity.Shipping;
import com.example.shopapp.entity.UserVo;
import com.example.shopapp.service.IShippingService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/shipping")
public class ShippingController {
    @Autowired
    IShippingService shippingService;

    @ResponseBody
    @RequestMapping("addShipping")
    public ServerResponse addShipping(HttpSession session,Shipping shipping){
        UserVo userVO=(UserVo) session.getAttribute(Const.LOGIN_SUCCESS);
        return shippingService.addShipping(userVO.getId(),shipping);
    }
    @ResponseBody
    @RequestMapping("delShipping")
    public ServerResponse delShipping(Integer shippingId,HttpSession session){
        UserVo userVO=(UserVo) session.getAttribute(Const.LOGIN_SUCCESS);
        return shippingService.delShipping(userVO.getId(),shippingId);
    }

    @ResponseBody
    @RequestMapping("updateShipping")
    public ServerResponse updateShipping(Shipping shipping, HttpSession session){
        UserVo userVO=(UserVo) session.getAttribute(Const.LOGIN_SUCCESS);
        return shippingService.updateShipping(userVO.getId(),shipping);
    }
    @ResponseBody
    @RequestMapping("selectShipping")
    public ServerResponse selectShipping(Integer shippingId,HttpSession session){
        UserVo userVO=(UserVo) session.getAttribute(Const.LOGIN_SUCCESS);
        return shippingService.selectShipping(userVO.getId(),shippingId);
    }
    @ResponseBody
    @RequestMapping("shippingList")
    public ServerResponse<PageInfo>  shippingList(HttpSession session,@RequestParam(value = "pageNum",defaultValue = "1",required = false )Integer pageNum,
                                                  @RequestParam(value = "pageSize",defaultValue = "10",required = false) Integer pageSize
                                                  ){
        UserVo userVO=(UserVo) session.getAttribute(Const.LOGIN_SUCCESS);
        return shippingService.shippingList(userVO.getId(),pageNum,pageSize);
    }
    @ResponseBody
    @RequestMapping("updateDefault")
    public ServerResponse updateAddressDefault(HttpSession session,Integer shippingId){
        UserVo userVO=(UserVo) session.getAttribute(Const.LOGIN_SUCCESS);
        return shippingService.updateUserDefault(userVO.getId(),shippingId);
    }
}
