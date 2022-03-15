package com.example.shopapp.controller;

import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.example.shopapp.Util.ServerResponse;
import com.example.shopapp.service.IPayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;


@Controller
@RequestMapping("/pay")
public class PayController {

    @Autowired
    IPayService payService;
    @Value("${alipay.publicKey}")
    private String publicKey;
    /**
     * 支付接口
     * **/

    @ResponseBody
    @RequestMapping("pay.do")
    public ServerResponse pay(Long orderNo){

        return payService.pay(orderNo);
    }
    @ResponseBody
    @RequestMapping("callback.do")
    public String alipayCallBack(HttpServletRequest request){
        Map<String,String[]> params=request.getParameterMap();
        if(params==null||params.size()==0){
            return "fail";
        }
        Map<String,String> signParam=new HashMap<>();
        Set<String> keys=params.keySet();
        Iterator<String> iterator=keys.iterator();
        while (iterator.hasNext()){
            String key=iterator.next();
            String[] values=params.get(key);
            StringBuilder stringBuilder=new StringBuilder();
            for(String value:values){
                stringBuilder.append(value+",");
            }
            String value=stringBuilder.toString();
            value= value.substring(0,value.length()-1);
            signParam.put(key,value);

        }
        //验证签名
        try {
            signParam.remove("sign_type");
            boolean checkSign=AlipaySignature.rsaCheckV2(signParam,publicKey,"utf-8","RSA2");
            if(checkSign){
                //通过
                //处理业务逻辑
                System.out.println("验证签名通过");
                return payService.callbackLogin(signParam);

            }
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }

        String sucess="success";
        return sucess;

    }
}
