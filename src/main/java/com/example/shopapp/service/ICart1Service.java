package com.example.shopapp.service;

import com.example.shopapp.Util.ServerResponse;
import com.example.shopapp.entity.CartVo;
import org.apache.ibatis.annotations.Select;

public interface ICart1Service {
    ServerResponse<CartVo> add(Integer userId, Integer goodsId, Integer quantity);
    ServerResponse<CartVo> update(Integer userId, Integer goodsId, Integer count);
    ServerResponse<CartVo> deteleGoods(Integer userId, String goodsIds);

    ServerResponse<CartVo> list(Integer userId);
    ServerResponse<CartVo> selectOrUnSelect(Integer userId, Integer goodsId, Integer checked);
    ServerResponse<Integer> getCartProductCount(Integer userId);

    public ServerResponse choice (Integer goodsId,Integer userId);
    ServerResponse findCartByUserIdAndChecked(Integer userId);
}
