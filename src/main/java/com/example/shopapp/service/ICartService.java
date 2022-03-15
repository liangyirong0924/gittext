package com.example.shopapp.service;

import com.example.shopapp.entity.BaseModel;
import com.example.shopapp.entity.Cart;


import java.util.List;

public interface ICartService  {
    int addToCart(Cart cart);
    List<Cart>  findByUserId(Integer userId);
   int updateNum(Cart cart);

}
