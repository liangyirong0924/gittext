package com.example.shopapp.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.example.shopapp.Util.BigDecimalUtil;
import com.example.shopapp.dao.CartMapper;
import com.example.shopapp.entity.Cart;
import com.example.shopapp.entity.GoodsList;
import com.example.shopapp.service.ICartService;
import com.example.shopapp.service.IGoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Service
public class CartServiceimpl implements ICartService {
    @Autowired
    CartMapper cartMapper;
    @Autowired
    IGoodsService goodsService;
   private int i=0;
    @Override
    public int addToCart(Cart cart) {
        QueryWrapper<Cart> wrapper =new QueryWrapper<>();
        wrapper.eq("user_id",cart.getUserId());
        wrapper.eq("goods_id",cart.getGoodsId());
        Cart cartDB=cartMapper.selectOne(wrapper);

        Date now=new Date();
          if (cartDB==null) {
              GoodsList goodsList = goodsService.SelectByGoodsId(cart.getGoodsId());
              Cart cart1 = new Cart();
              cart1.setUserId(cart.getUserId());
              cart1.setGoodsId(cart.getGoodsId());
              cart1.setQuantity(cart.getQuantity());
              cart1.setChecked(1);

              cart1.setCreateTime(now);
              cart1.setUpdateTime(now);
             i=  cartMapper.insert(cart1);

          } else {
            Integer quantity =cart.getQuantity()+cartDB.getQuantity();
//            Double price=cart.getPrice()+cartDB.getPrice();
//            Cart cart2 =new Cart();
//              cart2.setQuantity(num);
//              cart2.setUpdateTime(now);
//              BigDecimal price= BigDecimalUtil.mul(quantity,cart.getPrice());
              Cart temp =new Cart();
              temp.setQuantity(quantity);
//              temp.setPrice(price);
              temp.setCartId(cartDB.getCartId());
              i= cartMapper.updateById(temp);

        }

      return  i;
    }



    public List<Cart>  findByUserId(Integer userId){
        QueryWrapper<Cart> wrapper =new QueryWrapper<>();
        wrapper.eq("user_id",userId);

        return cartMapper.selectList(wrapper);
    }

    @Override
    public int updateNum(Cart cart) {
           Cart temp =new Cart();
           temp.setQuantity(cart.getQuantity());
           QueryWrapper queryWrapper=new QueryWrapper();
           queryWrapper.eq("user_id",cart.getUserId());
           queryWrapper.eq("cart_id",cart.getCartId());
           i=cartMapper.update(temp,queryWrapper);
        return i;
    }

    private int updateNumByCid(Integer cartId, Integer quantity){
        Cart cart=new Cart();
        cart.setQuantity(quantity);
        Date now =new Date();
        UpdateWrapper<Cart> qw =new UpdateWrapper<>();
        qw.eq("cart_id",cartId);

       i= cartMapper.update(cart,qw);

    return i;
    }
}
