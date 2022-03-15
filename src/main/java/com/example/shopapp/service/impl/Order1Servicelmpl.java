package com.example.shopapp.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.shopapp.Util.*;
import com.example.shopapp.dao.*;
import com.example.shopapp.entity.*;
import com.example.shopapp.service.ICart1Service;
import com.example.shopapp.service.IGoodsService;
import com.example.shopapp.service.IOrder1Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

@Service
public class Order1Servicelmpl  implements IOrder1Service {
    @Autowired
    OrderMapper orderMapper;
    @Autowired
    IGoodsService goodsService;
    @Autowired
    ICart1Service cart1Service;
    @Autowired
    OrderItemMapper orderItemMapper;
    @Autowired
    CartMapper cartMapper;
    @Autowired
    ShippingMapper shippingMapper;
    @Autowired
    GoodsMapper goodsMapper;
    @Override
    public ServerResponse getOrderCartGoods(Integer userId) {
        OrderGoodsVo orderGoodsVo=new OrderGoodsVo();
        //获取购物车中的数据
        ServerResponse serverResponse1=cart1Service.findCartByUserIdAndChecked(userId);
        List<Cart>cartList= (List<Cart>) serverResponse1.getData();
        ServerResponse serverResponse=this.getCartOrderItem(userId,cartList);
        if (!serverResponse.isSuccess()){
            return serverResponse;
        }
        List<OrderItem> orderItemList= (List<OrderItem>) serverResponse.getData();
        List<OrderItemVo> orderItemVoList=new ArrayList<>();
        BigDecimal payment=new BigDecimal("0");
        for (OrderItem orderItem:orderItemList){
                     payment=BigDecimalUtil.add(payment.doubleValue(),orderItem.getTotalPrice().doubleValue());
                     orderItemVoList.add(createOrderItemVo(orderItem));
        }
        orderGoodsVo.setGoodsTotalPrice(payment);
        orderGoodsVo.setOrderItemVoList(orderItemVoList);

        return ServerResponse.createBySuccess(orderGoodsVo);
    }

    @Override
    @Transactional(rollbackFor = {GoodsException.class})
    public  ServerResponse createOrderByCart(Integer userId,Integer shippingId){
        //从购物车中获取数据
        ServerResponse serverResponse1= cart1Service.findCartByUserIdAndChecked(userId);
        List<Cart> cartList= (List<Cart>) serverResponse1.getData();
        //计算这个订单总价
        ServerResponse serverResponse=this.getCartOrderItem(userId,cartList);
        if (!serverResponse.isSuccess()){
            return serverResponse;
        }
        List<OrderItem> orderItemList= (List<OrderItem>) serverResponse.getData();
        BigDecimal payment=this.getOrderTotalPrice(orderItemList);

        //生成订单
        Order1 order1 =this.createOrder(userId,shippingId,payment);
        if(order1 == null){

            throw  new GoodsException("生成订单失败");
        }
        if(CollectionUtils.isEmpty(orderItemList)){
            return ServerResponse.createByErrorMessage("购物车为空");
        }
        for(OrderItem orderItem : orderItemList){
            orderItem.setOrderNo(order1.getOrderNo());
        }
        int count=orderItemMapper.insertBatch(orderItemList);
        if (count!=orderItemList.size()){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ORDERITEM_INSERT_FAIL.getCode(),ResponseCode.ORDERITEM_INSERT_FAIL.getMsg());
        }
        //生成成功,我们要减少我们产品的库存
        this.reduceGoodsStock(orderItemList);
        //清空一下购物车
        this.cleanCart(cartList);

        OrderVo orderVo=createOrderVo(order1,orderItemList);
        return ServerResponse.createBySuccess(orderVo);
    }

    @Override
    public ServerResponse<String> cancel(Integer userId, Long orderNo) {
        QueryWrapper<Order1> wrapper=new QueryWrapper<>();
        wrapper.eq("user_id",userId).eq("order_no",orderNo);
        Order1 order1 =orderMapper.selectOne(wrapper);
        if (order1 ==null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ORDER_NOT_EXISTS.getCode(),ResponseCode.ORDER_NOT_EXISTS.getMsg());
        }
        if (order1.getStatus()==OrderStatusEnum.ORDER_PAYED.getStatus()){
            return ServerResponse.createByErrorMessage("已经付款,无法取消订单");
        }
        if (order1.getStatus()==OrderStatusEnum.ORDER_CANCELED.getStatus()){
            return ServerResponse.createByErrorMessage("已经取消订单，无需重复");
        }
        Order1 updateOrder1 =new Order1();
        updateOrder1.setId(order1.getId());
        updateOrder1.setStatus(OrderStatusEnum.ORDER_CANCELED.getStatus());
        int row=orderMapper.updateById(updateOrder1);
        if(row > 0){
            return ServerResponse.createBySuccessMessage("取消订单成功");
        }
        return ServerResponse.createByErrorMessage("取消订单失败");
    }


    @Override
    public ServerResponse<OrderVo> getOrderDetail(Integer userId, Long orderNo) {
        QueryWrapper<Order1> wrapper=new QueryWrapper<>();
        wrapper.eq("user_id",userId).eq("order_no",orderNo);
        Order1 order1 =orderMapper.selectOne(wrapper);
        if (order1 !=null){
            QueryWrapper<OrderItem> wrapper1=new QueryWrapper<>();
            wrapper1.eq("order_no",orderNo).eq("user_id",userId);
            List<OrderItem> orderItemList=orderItemMapper.selectList(wrapper1);
            OrderVo orderVo=createOrderVo(order1,orderItemList);
            return ServerResponse.createBySuccess(orderVo);
        }
        return ServerResponse.createByErrorMessage("没有找到该订单");
    }

//    @Override
//    public ServerResponse<OrderVo> getOrderList(Integer userId, Integer status) {
//        QueryWrapper<Order1> orderQueryWrapper=new QueryWrapper<>();
//        orderQueryWrapper.eq("user_id",userId).eq("status",status);
//       IPage<Order1> page=new Page<>(1,2);
//        IPage<Order1> orderlist1=orderMapper.selectPage(page,orderQueryWrapper);
//        List<Order1> order1s=orderlist1.getRecords();
//        List<OrderVo> orderVoList=createOrderVoList(order1s,userId);
////        List<Order1> order1List =orderMapper.selectList(orderQueryWrapper);
////        List<OrderVo> orderVoList=createOrderVoList(order1List,userId);
//
//        return ServerResponse.createBySuccess(orderVoList);
//    }

    @Override
    public ServerResponse confirmReceivedGoods(Integer userId, Long orderNo) {
         QueryWrapper<Order1> wrapper=new QueryWrapper<>();
         wrapper.eq("order_no",orderNo).eq("user_id",userId);
         Order1 order1 =new Order1();
         order1.setStatus(40);
         int rowCount=orderMapper.update(order1,wrapper);
        if(rowCount > 0){
            return ServerResponse.createBySuccessMessage("已经确认收货");
        }
        return ServerResponse.createByErrorMessage("确认收货失败");
    }

    @Override
    public ServerResponse findOrderByOrderno(Long orderNo) {
        if (orderNo==null){
            return ServerResponse.createByError(ResponseCode.ORDER_NO_NOT_EMPTY.getCode(),ResponseCode.ORDER_NO_NOT_EMPTY.getMsg());
        }
        QueryWrapper<Order1> wrapper=new QueryWrapper<>();
        wrapper.eq("order_no",orderNo);
        Order1 order1=orderMapper.selectOne(wrapper);
        if (order1==null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ORDER_NOT_EXISTS.getCode(),ResponseCode.ORDER_NOT_EXISTS.getMsg());
        }
        return ServerResponse.createBySuccess(order1);
    }

    @Override
    public ServerResponse updateOrderPayStatus(Long orderNo,Integer status, Date payTime) {
        UpdateWrapper<Order1> queryWrapper=new UpdateWrapper<>();
        queryWrapper.eq("order_no",orderNo);
        Order1 order1=new Order1();
        order1.setStatus(status);
        order1.setPaymentTime(payTime);
        int count=orderMapper.update(order1,queryWrapper);
        if (count==0){
            return ServerResponse.createServerResponseByFail(ResponseCode.ORDER_UPDATE_FAIL.getCode(),ResponseCode.ORDER_UPDATE_FAIL.getMsg());

        }
        return ServerResponse.createBySuccess();
    }

    @Transactional(rollbackFor = {GoodsException.class})
    public ServerResponse createOrderDirect(Integer userId,Integer goodsId,Integer quantity, Integer shippingId){
         GoodsList goodsList=goodsService.SelectByGoodsId(goodsId);
         if (ResponseCode.ON_SALE.getCode()!=goodsList.getStatus()){
             return ServerResponse.createByErrorMessage("产品"+goodsList.getText()+"不在售卖状态");
         }
         //校验库存
        if (quantity>goodsList.getStock()){
            return ServerResponse.createByErrorMessage("产品"+goodsList.getText()+"库存不足");
        }
        BigDecimal payment =BigDecimalUtil.mul(goodsList.getPrice().doubleValue(),quantity);

        //订单插入
        Order1 order1 =this.createOrder(userId,shippingId,payment);
        if (order1 ==null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ORDERITEM_INSERT_FAIL.getCode(),ResponseCode.ORDERITEM_INSERT_FAIL.getMsg());
        }
        //OrderItem插入
        OrderItem orderItem = new OrderItem();
        orderItem.setUserId(userId);
        orderItem.setGoodsId(goodsList.getGoodsId());
        orderItem.setGoodsText(goodsList.getText());
        orderItem.setGoodsImage(goodsList.getImage());
        orderItem.setCurrentUnitPrice(goodsList.getPrice());
        orderItem.setQuantity(quantity);
        orderItem.setTotalPrice(payment);
        orderItem.setOrderNo(order1.getOrderNo());
        orderItem.setCreateTime(new Date());
        orderItem.setUpdateTime(new Date());
        orderItemMapper.insert(orderItem);

        goodsList.setStock(goodsList.getStock()-orderItem.getQuantity());
        goodsMapper.updateById(goodsList);

        List<OrderItem> orderItemList=new ArrayList<>();
        orderItemList.add(orderItem);
        OrderVo orderVo=createOrderVo(order1,orderItemList);
        if (orderVo==null){
            throw new GoodsException("创建订单明细失败");
        }
        return ServerResponse.createBySuccess(orderVo);

    }

    @Override
    public ServerResponse<PageInfo> getOrderList(Integer userId, Integer status, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        List<Order1> orderList=new ArrayList<>();
        if (status==-1){
            QueryWrapper wrapper=new QueryWrapper();
            wrapper.eq("user_id",userId);
            orderList=orderMapper.selectList(wrapper);
        }else {
            orderList = orderMapper.selectByUserIdAndStatus(userId,status);
        }
        List<OrderVo> orderVoList = createOrderVoList(orderList,userId);
        PageInfo pageResult = new PageInfo(orderList);
        pageResult.setList(orderVoList);
        return ServerResponse.createBySuccess(pageResult);
    }

    private OrderVo createOrderVo(Order1 order1, List<OrderItem> orderItemList) {
        OrderVo orderVo=new OrderVo();
        orderVo.setOrderNo(order1.getOrderNo());
        orderVo.setPayment(order1.getPayment());
        orderVo.setPaymentType(order1.getPaymentType());
        orderVo.setPostage(order1.getPostage());
        orderVo.setStatus(order1.getStatus());
        orderVo.setStatusDesc(OrderStatusEnum.getStatusDesc(order1.getStatus()));
        orderVo.setShippingId(order1.getShippingId());
        Shipping shipping=shippingMapper.selectById(order1.getShippingId());
        if (shipping!=null){
            orderVo.setReceiveName(shipping.getReceiverName());
            orderVo.setShippingVo(createShippingVo(shipping));
        }
        orderVo.setPaymentTime(DateTimeUtil.dateToStr(order1.getPaymentTime()));
        orderVo.setSendTime(DateTimeUtil.dateToStr(order1.getSendTime()));
        orderVo.setEndTime(DateTimeUtil.dateToStr(order1.getEndTime()));
        orderVo.setCreateTime(DateTimeUtil.dateToStr(order1.getCreateTime()));
        orderVo.setCloseTime(DateTimeUtil.dateToStr(order1.getCloseTime()));

        List<OrderItemVo> orderItemVoList=new ArrayList<>();
        for (OrderItem orderItem:orderItemList){
             OrderItemVo orderItemVo=createOrderItemVo(orderItem);
             orderItemVoList.add(orderItemVo);
        }

        orderVo.setOrderItemVoList(orderItemVoList);
        return orderVo;
    }
private ShippingVo createShippingVo(Shipping shipping){
        ShippingVo shippingVo=new ShippingVo();
        shippingVo.setReceiverName(shipping.getReceiverName());
        shippingVo.setReceiverAddress(shipping.getReceiverAddress());
        shippingVo.setReceiverAddress(shipping.getReceiverProvince());
        shippingVo.setReceiverCity(shipping.getReceiverCity());
        shippingVo.setReceiverDistrict(shipping.getReceiverDistrict());
        shippingVo.setReceiverZip(shipping.getReceiverZip());
        shippingVo.setReceiverPhone(shipping.getReceiverPhone());
    return shippingVo;

}
    private void  reduceGoodsStock(List<OrderItem> orderItemList){
        for (OrderItem orderItem:orderItemList){
            GoodsList goodsList= goodsService.SelectByGoodsId(orderItem.getGoodsId());
            if (goodsList==null){
                throw  new GoodsException("商品不存在");
            }
            //购买数量
            int quantity=  orderItem.getQuantity();
            if (goodsList.getStock()<quantity){
                //商品库存不够
                throw  new GoodsException("商品库存不足");
            }
            //更新库存
            ServerResponse serverResponse= goodsService.updateGoodsStock(goodsList.getGoodsId(),goodsList.getStock()-quantity);
            if (serverResponse==null){
                throw new GoodsException("商品库存扣除失败！");
            }
        }

    }
    private void cleanCart(List<Cart> cartList){
        for (Cart cart:cartList){
            cartMapper.deleteById(cart.getCartId());
        }
    }
    private List<OrderVo> createOrderVoList(List<Order1> order1List, Integer userId){
        List<OrderVo> orderVoList=new ArrayList<>();
        for (Order1 order1 : order1List){
            List<OrderItem>orderItemList=new ArrayList<>();
            QueryWrapper<OrderItem> wrapper=new QueryWrapper();
            wrapper.eq("order_no", order1.getOrderNo()).eq("user_id",userId);
            orderItemList=orderItemMapper.selectList(wrapper);
            OrderVo orderVo=createOrderVo(order1,orderItemList);
            orderVoList.add(orderVo);
        }
     return orderVoList;
    }
    private Order1 createOrder(Integer userId, Integer shippingId, BigDecimal payment){
        Order1 order1=new Order1();
        long orderNo=this.generateOrderNo();
        order1.setOrderNo(orderNo);
        order1.setStatus(OrderStatusEnum.ORDER_NO_PAYED.getStatus());
       order1.setPostage(0);
       order1.setPaymentType(ResponseCode.NLINE_PAY.getCode());
       order1.setPayment(payment);
       order1.setUserId(userId);
       order1.setShippingId(shippingId);
        int rowCount=orderMapper.insert(order1);
        if (rowCount>0){
            return order1;
        }
        return null;
    }

    private long generateOrderNo(){
        long currentTime =System.currentTimeMillis();
        return currentTime+new Random().nextInt(100);
    }
    private BigDecimal getOrderTotalPrice(List<OrderItem> orderItemList){
        BigDecimal payment = new BigDecimal("0");
        for(OrderItem orderItem : orderItemList){
            payment = BigDecimalUtil.add(payment.doubleValue(),orderItem.getTotalPrice().doubleValue());
        }
        return payment;
    }
    private OrderItemVo createOrderItemVo(OrderItem orderItem){
        OrderItemVo orderItemVo = new OrderItemVo();
        orderItemVo.setOrderNo(orderItem.getOrderNo());
        orderItemVo.setGoodsId(orderItem.getGoodsId());
        orderItemVo.setGoodsText(orderItem.getGoodsText());
        orderItemVo.setGoodsImage(orderItem.getGoodsImage());
        orderItemVo.setCurrentUnitPrice(orderItem.getCurrentUnitPrice());
        orderItemVo.setQuantity(orderItem.getQuantity());

        orderItemVo.setTotalPrice(orderItem.getTotalPrice());

        orderItemVo.setCreateTime(DateTimeUtil.dateToStr(orderItem.getCreateTime()));
        return orderItemVo;
    }

    private ServerResponse getCartOrderItem(Integer userId,List<Cart> cartList){
        List<OrderItem> orderItemList=new ArrayList<>();
        if (CollectionUtils.isEmpty(cartList)){
                return ServerResponse.createByErrorCodeMessage(ResponseCode.CART_NOT_SELECTED.getCode(),ResponseCode.CART_NOT_SELECTED.getMsg());
        }
        for (Cart cartItem:cartList){
            OrderItem orderItem=new OrderItem();
            GoodsList goodsList=goodsService.SelectByGoodsId(cartItem.getGoodsId());
            if (ResponseCode.ON_SALE.getCode()!=goodsList.getStatus()){
                return ServerResponse.createByErrorMessage("产品"+goodsList.getText()+"不在售卖状态");
            }
            if (cartItem.getQuantity()>goodsList.getStock()){
                return ServerResponse.createByErrorMessage("产品"+goodsList.getText()+"库存不足");
            }
            orderItem.setUserId(userId);

            orderItem.setGoodsId(goodsList.getGoodsId());
            orderItem.setGoodsText(goodsList.getText());
            orderItem.setGoodsImage(goodsList.getImage());
            orderItem.setCurrentUnitPrice(goodsList.getPrice());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setTotalPrice(BigDecimalUtil.mul(goodsList.getPrice().doubleValue(),cartItem.getQuantity()));
            orderItemList.add(orderItem);
        }
        return ServerResponse.createBySuccess(orderItemList);
    }


}
