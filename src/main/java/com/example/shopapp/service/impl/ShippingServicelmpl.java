package com.example.shopapp.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.shopapp.Util.ServerResponse;
import com.example.shopapp.dao.ShippingMapper;
import com.example.shopapp.entity.Shipping;
import com.example.shopapp.service.IShippingService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class ShippingServicelmpl implements IShippingService {
    @Autowired
    ShippingMapper shippingMapper;
    @Override
    public ServerResponse addShipping(Integer userId, Shipping shipping) {
        shipping.setUserId(userId);
        QueryWrapper<Shipping> wrapper=new QueryWrapper<>();
        wrapper.eq("user_id",userId);
        List<Shipping> shippingList=shippingMapper.selectList(wrapper);
        if (shippingList==null||shippingList.size()<0){
            shipping.setIsDefault(1);
        }
        Shipping shipping1=new Shipping();
        shipping1.setReceiverAddress(shipping.getReceiverAddress());
        shipping1.setReceiverCity(shipping.getReceiverCity());
        shipping1.setUserId(userId);
        shipping1.setCreateTime(new Date());
        shipping1.setUpdateTime(new Date());
        shipping1.setReceiverDistrict(shipping.getReceiverDistrict());
        shipping1.setReceiverName(shipping.getReceiverName());
        shipping1.setReceiverProvince(shipping.getReceiverProvince());
        shipping1.setReceiverZip(shipping.getReceiverZip());
        shipping1.setIsDefault(shipping.getIsDefault());

        int rowCount=shippingMapper.insert(shipping);
        if (rowCount>0){
            return ServerResponse.createBySuccess("新建地址成功",shipping.getId());
        }
        return ServerResponse.createBySuccessMessage("新建地址失败");
    }

    @Override
    public ServerResponse<String> delShipping(Integer userId, Integer shippingId) {
        QueryWrapper<Shipping> wrapper=new QueryWrapper<>();
        wrapper.eq("user_id",userId).eq("id",shippingId);
        int resultCount=shippingMapper.delete(wrapper);
        if (resultCount>0){
            return ServerResponse.createBySuccess("删除地址成功");
        }
        return ServerResponse.createBySuccessMessage("删除地址失败");
    }

    @Override
    public ServerResponse updateShipping(Integer userId, Shipping shipping) {
        shipping.setUserId(userId);
        Shipping shipping1=new Shipping();
        shipping1.setReceiverAddress(shipping.getReceiverAddress());
        shipping1.setReceiverCity(shipping.getReceiverCity());
        shipping1.setUserId(userId);
        shipping1.setCreateTime(new Date());
        shipping1.setUpdateTime(new Date());
        shipping1.setReceiverDistrict(shipping.getReceiverDistrict());
        shipping1.setReceiverName(shipping.getReceiverName());
        shipping1.setReceiverProvince(shipping.getReceiverProvince());
        shipping1.setReceiverZip(shipping.getReceiverZip());
        shipping1.setIsDefault(shipping.getIsDefault());
        QueryWrapper<Shipping> wrapper=new QueryWrapper();
        wrapper.eq("user_id",userId);
        int rowCount = shippingMapper.update(shipping1,wrapper);
        if (rowCount>0){
            return ServerResponse.createBySuccess("修改地址成功");
        }
        return ServerResponse.createBySuccessMessage("修改地址失败");
    }

    @Override
    public ServerResponse<Shipping> selectShipping(Integer userId, Integer shippingId) {
        QueryWrapper<Shipping>wrapper=new QueryWrapper<>();
        wrapper.eq("user_id",userId).eq("id",shippingId);
        Shipping shipping=shippingMapper.selectOne(wrapper);
        if(shipping == null){
            return ServerResponse.createByErrorMessage("无法查询到该地址");
        }
        return ServerResponse.createBySuccess("查询地址成功",shipping);
    }

    @Override
    public ServerResponse<PageInfo> shippingList(Integer userId, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        QueryWrapper<Shipping> wrapper=new QueryWrapper<>();
        wrapper.eq("user_id",userId);
       List<Shipping> shippingList=shippingMapper.selectList(wrapper);
      PageInfo pageInfo=new PageInfo(shippingList);
        return ServerResponse.createBySuccess(pageInfo);
    }

    @Override
    public ServerResponse updateUserDefault(Integer userId, Integer shippingId) {
        QueryWrapper<Shipping> wrapper=new QueryWrapper<>();
        Shipping shipping=new Shipping();
        shipping.setIsDefault(1);
        wrapper.eq("user_id",userId);
        List<Shipping> list=shippingMapper.selectList(wrapper);
        for (Shipping shipping1:list){
            shipping1.setIsDefault(0);
//            QueryWrapper queryWrapper=new QueryWrapper();
//            queryWrapper.eq("id",);
            shippingMapper.updateById(shipping1);
        }
        UpdateWrapper<Shipping> Wrapper=new UpdateWrapper<>();
        Shipping shipping1=new Shipping();
        Wrapper.eq("id",shippingId).eq("user_id",userId);
        shipping1.setIsDefault(1);
        int count=shippingMapper.update(shipping1,Wrapper);
        if (count==0){
            return ServerResponse.createByErrorMessage("更改默认地址失败");
        }
        return ServerResponse.createBySuccessMessage("更改默认地址成功");
    }
}
