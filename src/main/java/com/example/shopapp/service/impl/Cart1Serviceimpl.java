package com.example.shopapp.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.example.shopapp.Util.*;
import com.example.shopapp.dao.CartMapper;
import com.example.shopapp.dao.GoodsMapper;
import com.example.shopapp.entity.*;
import com.example.shopapp.service.ICart1Service;
import com.google.common.base.Splitter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Service
public class Cart1Serviceimpl implements ICart1Service {
    @Autowired
    CartMapper cartMapper;
    @Autowired
    GoodsMapper goodsMapper;

    @Override
    public ServerResponse<CartVo> add(Integer userId, Integer goodsId, Integer quantity) {
           if (goodsId==null||quantity==null){
               return ServerResponse.createByErrorCodeMessage(ResponseCode.PARAMTER_NOT_EMPTY.getCode(),ResponseCode.PARAMTER_NOT_EMPTY.getMsg());
           }
           QueryWrapper<Cart> qw =new QueryWrapper<>();
           qw.eq("user_id",userId);
           qw.eq("goods_id",goodsId);
           Cart cart=cartMapper.selectOne(qw);
           if (cart==null){
               Cart cartitem=new Cart();
               cartitem.setQuantity(quantity);
               cartitem.setChecked(Const.Cart.CHECKED);
               cartitem.setUserId(userId);
               cartitem.setGoodsId(goodsId);
               cartitem.setCreateTime(new Date());
               cartitem.setUpdateTime(new Date());
               cartMapper.insert(cartitem);
           }else {
               quantity=cart.getQuantity()+quantity;
               cart.setQuantity(quantity);
               cart.setUpdateTime(new Date());
               cartMapper.updateById(cart);
           }
        return this.list(userId);
    }

    @Override
    public ServerResponse<CartVo> update(Integer userId, Integer goodsId, Integer count) {
        if (goodsId==null||count==null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.CART_UPDATE_FAIL.getCode(), ResponseCode.CART_UPDATE_FAIL.getMsg());
        }
        Cart temp =new Cart();
        temp.setQuantity(count);
        temp.setUpdateTime(new Date());
        QueryWrapper queryWrapper=new QueryWrapper();
        queryWrapper.eq("user_id",userId);
        queryWrapper.eq("goods_id",goodsId);
       cartMapper.update(temp,queryWrapper);
        return this.list(userId);
    }

    @Override
    public ServerResponse<CartVo> deteleGoods(Integer userId, String goodsIds) {

         List<String> goodsList = Splitter.on(",").splitToList(goodsIds);
         if (CollectionUtils.isEmpty(goodsList)){
             return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_PARAM.getCode(), ResponseCode.ILLEGAL_PARAM.getMsg());
         }
    cartMapper.deleteByUserIdGoodsIds(userId,goodsList);
        return list(userId);
    }

    @Override
    public ServerResponse<CartVo> list(Integer userId) {
        CartVo cartVo=this.getCartVoLimit(userId);
        return ServerResponse.createBySuccess(cartVo);
    }

    @Override
    public ServerResponse<CartVo> selectOrUnSelect(Integer userId, Integer goodsId, Integer checked) {
        UpdateWrapper updateWrapper=new UpdateWrapper();
        Cart temp =new Cart();
        updateWrapper.eq("user_id",userId);
        updateWrapper.eq("goods_id",goodsId);
        temp.setChecked(checked);
        temp.setUpdateTime(new Date());
        cartMapper.update(temp,updateWrapper);
        return this.list(userId);
    }

    @Override
    public ServerResponse<Integer> getCartProductCount(Integer userId) {
        if (userId==null){
            return ServerResponse.createBySuccess(0);
        }

        return ServerResponse.createBySuccess(cartMapper.selectCartProductCount(userId));
    }

    @Override
    public ServerResponse choice(Integer goodsId,Integer userId) {
        if (goodsId==0){
            //全选或者反选
            //查看当前状态：全选还是全不选
//            int count=cartMapper.unCheckedCount(userId);
          int count=this.getAllCheckedStatus(userId);
            if (count==0){
                 this.updateCheckedByUserId(userId, CheckEnum.CART_PRODUCT_UNCHECK.getCheck());
            }else {
             this.updateCheckedByUserId(userId, CheckEnum.CART_PRODUCT_CHECKED.getCheck());
            }
        }else {
            //单选操作
            int result =cartMapper.findCheckByGoodsId(goodsId,userId);
            if (result==0){
                this.updateCheckedByUserIdAndGoodsId(userId,goodsId,CheckEnum.CART_PRODUCT_CHECKED.getCheck());
            }else {
                this.updateCheckedByUserIdAndGoodsId(userId,goodsId,CheckEnum.CART_PRODUCT_UNCHECK.getCheck());
            }
        }
        return ServerResponse.createBySuccess(getCartVoLimit(userId));
    }

    @Override
    public ServerResponse findCartByUserIdAndChecked(Integer userId) {
        QueryWrapper<Cart> wrapper=new QueryWrapper<>();
        wrapper.eq("user_id",userId);
        wrapper.eq("checked",1);
        List<Cart> cartList=cartMapper.selectList(wrapper);
        if (cartList==null||cartList.size()==0){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.CART_NOT_SELECTED.getCode(),ResponseCode.CART_NOT_SELECTED.getMsg());
        }
        return ServerResponse.createBySuccess(cartList);
    }

    private  CartVo getCartVoLimit(Integer userId){
        CartVo cartVo=new CartVo();
        QueryWrapper<Cart> wrapper=new QueryWrapper(); //存储用户购物车信息
        wrapper.eq("user_id",userId);
        List<Cart> cartList=cartMapper.selectList(wrapper);
        List<CartGoodsListVo> cartGoodsListVos=new ArrayList<>();
        BigDecimal cartTotalPrice=new BigDecimal("0");
        if(cartList!=null){
            for (Cart cartltem:cartList){ //展示数据
                CartGoodsListVo cartGoodsListVo=new CartGoodsListVo();
                cartGoodsListVo.setCartId(cartltem.getCartId());
                cartGoodsListVo.setUserId(userId);
                cartGoodsListVo.setQuantity(cartltem.getQuantity());
                cartGoodsListVo.setGoodsChecked(cartltem.getChecked());


                GoodsList goodsList = goodsMapper.SelectByGoodsId(cartltem.getGoodsId());
                if (goodsList!=null){
                    cartGoodsListVo.setGoodsImage(goodsList.getImage());
                    cartGoodsListVo.setGoodsText(goodsList.getText());
                    cartGoodsListVo.setGoodsStatus(goodsList.getStatus());
                    cartGoodsListVo.setGoodsPrice(goodsList.getPrice());
                    cartGoodsListVo.setGoodsId(goodsList.getGoodsId());
                    cartGoodsListVo.setGoodsStock(goodsList.getStock());
                    int buyLimitCount=0;
                    if (goodsList.getStock()>=cartltem.getQuantity()){
                        buyLimitCount=cartltem.getQuantity();
                        cartGoodsListVo.setLimitQuantity(Const.Cart.LIMIT_NUM_SUCCESS);
                    }else {
                        buyLimitCount=goodsList.getStock();
                        cartGoodsListVo.setLimitQuantity(Const.Cart.LIMIT_NUM_FAIL);
                        //更新数据库中购物车商品的数量
                        Cart cartForQuantity=new Cart();
                        cartForQuantity.setCartId(cartltem.getCartId());
                        cartForQuantity.setQuantity(buyLimitCount);
                        cartMapper.updateById(cartForQuantity);
                    }
                    cartGoodsListVo.setQuantity(buyLimitCount);

                    cartGoodsListVo.setGoodsTotalPrice(BigDecimalUtil.mul(goodsList.getPrice().doubleValue(),cartGoodsListVo.getQuantity()));
                }
                if (cartltem.getChecked()==Const.Cart.CHECKED){
                    cartTotalPrice=BigDecimalUtil.add(cartTotalPrice.doubleValue(),cartGoodsListVo.getGoodsTotalPrice().doubleValue());
                }
                cartGoodsListVos.add(cartGoodsListVo);
            }
        }
        cartVo.setCartGoodsListVos(cartGoodsListVos);
//        int unCheckedCount=cartMapper.unCheckedCount(userId);
        int unCheckedCount= this.getAllCheckedStatus(userId);
        if (unCheckedCount!=0){
            cartVo.setAllChecked(false);
        }else {
            cartVo.setAllChecked(true);
        }

     cartVo.setCartTotalPrice(cartTotalPrice);
        return cartVo;
    }
    private  int getAllCheckedStatus(Integer userId){

        QueryWrapper qw =new QueryWrapper();
        qw.eq("user_id",userId);
        qw.eq("checked",0);
        int i= Math.toIntExact(cartMapper.selectCount(qw));
          return  i;
    }
    private int updateCheckedByUserId(Integer userId,Integer checked){
          Cart temp =new Cart();
          UpdateWrapper wrapper=new UpdateWrapper();
          wrapper.eq("user_id",userId);
          temp.setChecked(checked);
          temp.setUpdateTime(new Date());
          int i=cartMapper.update(temp,wrapper);
          return  i;
    }
    private int updateCheckedByUserIdAndGoodsId(Integer userId, Integer goodsId, Integer checked) {
        UpdateWrapper updateWrapper=new UpdateWrapper();
        Cart temp =new Cart();
        updateWrapper.eq("user_id",userId);
        updateWrapper.eq("goods_id",goodsId);
        temp.setChecked(checked);
        temp.setUpdateTime(new Date());
       int i= cartMapper.update(temp,updateWrapper);
        return  i;
    }
//private  int findCheckByGoodsId(Integer goodsId,Integer userId){
//        Cart temp =new Cart();
//        QueryWrapper qw =new QueryWrapper();
//        qw.eq("user_id",userId);
//        qw.eq("goods_id",goodsId);
//         temp.getChecked();
//         int i= cartMapper.selectOne(qw);
//}


}