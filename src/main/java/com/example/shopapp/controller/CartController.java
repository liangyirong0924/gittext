package com.example.shopapp.controller;

import com.example.shopapp.Util.Const;
import com.example.shopapp.Util.ResponseCode;
import com.example.shopapp.Util.ServerResponse;
import com.example.shopapp.entity.BaseModel;
import com.example.shopapp.entity.Cart;
import com.example.shopapp.entity.CartVo;
import com.example.shopapp.entity.UserVo;
import com.example.shopapp.service.ICart1Service;
import com.example.shopapp.service.ICartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/cart")
public class CartController {
    @Autowired
    ICartService cartService;
    @Autowired
    ICart1Service cart1Service;
    @RequestMapping("addCart")
    @ResponseBody
    public BaseModel addToCart(Cart cart){
        int i=cartService.addToCart(cart);
        BaseModel model;
        if(i>0){
            model=BaseModel.createRespose(201,"加入购物车成功");
        }else {
            model = BaseModel.createRespose(200, "加入购物车失败");
        }
        return model;
    }
    @RequestMapping("show")
    @ResponseBody
    public BaseModel findCartByUserId(Integer userId){
        List<Cart> cartList =cartService.findByUserId(userId);
        BaseModel model;
        if (cartList == null) {
            model = BaseModel.createRespose(201, "查询购物车失败");

        }else {
            model =  BaseModel.createRespose(200, "查询购物车成功");

           model.setDatas(cartList);
        }
        return model;
    }
//    @RequestMapping("update")
//    @ResponseBody
//    public BaseModel updateNum(Cart cart){
//       int  i =cartService.updateNum(cart);
//        BaseModel model;
//        if (i>0) {
//            model = BaseModel.createRespose(201, "修改成功");
//
//        }else {
//            model =  BaseModel.createRespose(200, "修改失败");
//
//        }
//        return model;
//    }
@RequestMapping("list.do")
@ResponseBody
public ServerResponse<CartVo> list(HttpSession  session){
    UserVo userVO=(UserVo) session.getAttribute(Const.LOGIN_SUCCESS);
    return cart1Service.list(userVO.getId());
}
    @RequestMapping("add.do")
    @ResponseBody
    public ServerResponse<CartVo> add(HttpSession  session, Integer goodsId, Integer quantity ){
//     if (userId==null){
//         return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getMsg());
//     }
        UserVo userVO=(UserVo) session.getAttribute(Const.LOGIN_SUCCESS);
     return cart1Service.add(userVO.getId(),goodsId,quantity);
    }

    @RequestMapping("update.do")
    @ResponseBody
    public ServerResponse<CartVo> update(HttpSession  session,Integer goodsId,Integer count ){
        UserVo userVO=(UserVo) session.getAttribute(Const.LOGIN_SUCCESS);
        return cart1Service.update(userVO.getId(),goodsId,count);
    }

    @RequestMapping("delete_goods.do")
    @ResponseBody
    public ServerResponse<CartVo> deleteGoods(HttpSession  session,String goodsIds){
        UserVo userVO=(UserVo) session.getAttribute(Const.LOGIN_SUCCESS);
        return cart1Service.deteleGoods(userVO.getId(),goodsIds);
    }
//    @RequestMapping("select_all.do")
//    @ResponseBody
//    public ServerResponse<CartVo> selectAll(Integer userId){
//        if(userId ==null){
//            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
//        }
//        return cart1Service.selectOrUnSelect(userId,null, Const.Cart.CHECKED);
//    }
//    @RequestMapping("un_select_all.do")
//    @ResponseBody
//    public ServerResponse<CartVo> unSelectAll(Integer userId){
//        if(userId ==null){
//            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
//        }
//        return cart1Service.selectOrUnSelect(userId,null, Const.Cart.UN_CHECKED);
//    }
//    @RequestMapping("select.do")
//    @ResponseBody
//    public ServerResponse<CartVo> select(Integer userId, Integer goodsId){
//        if(userId ==null){
//            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
//        }
//        return cart1Service.selectOrUnSelect(userId,goodsId, Const.Cart.CHECKED);
//    }
//    @RequestMapping("un_select.do")
//    @ResponseBody
//    public ServerResponse<CartVo> unSelect(Integer userId, Integer productId){
//        if(userId ==null){
//            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
//        }
//        return cart1Service.selectOrUnSelect(userId,productId, Const.Cart.UN_CHECKED);
//    }
//    @RequestMapping("get_cart_product_count.do")
//    @ResponseBody
//    public ServerResponse<Integer> getCartProductCount(Integer userId){
//        if(userId ==null){
//            return ServerResponse.createBySuccess(0);
//        }
//        return cart1Service.getCartProductCount(userId);
//    }
    @RequestMapping("choice.do")
    @ResponseBody
    public ServerResponse<Integer> choice( @RequestParam(value = "goodsId",required = false,defaultValue = "0") Integer goodsId,HttpSession  session){
        UserVo userVO=(UserVo) session.getAttribute(Const.LOGIN_SUCCESS);
            return  cart1Service.choice(goodsId,userVO.getId());
    }
}
