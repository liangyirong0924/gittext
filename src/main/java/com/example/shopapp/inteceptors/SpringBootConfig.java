package com.example.shopapp.inteceptors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;
import java.util.List;

@SpringBootConfiguration
public class SpringBootConfig implements WebMvcConfigurer{


    @Autowired
    PortalLoginCheckInterceptor portalLoginCheckInterceptor;

//注册拦截器
    @Override
    public void addInterceptors(InterceptorRegistry registry) {

         List<String> includeUrl=new ArrayList<>();

        includeUrl.add("/cart/**");
        includeUrl.add("/order/**");
        includeUrl.add("/shipping/**");
        includeUrl.add("pay/**");


        List<String> exclueUrl=new ArrayList<>();
        exclueUrl.add("/user/login");
        exclueUrl.add("/user/register");
        exclueUrl.add("/goods");
        exclueUrl.add("/pay/callback.do");
//        exclueUrl.add("/portal/pay/callback.do");

        registry.addInterceptor(portalLoginCheckInterceptor).addPathPatterns(includeUrl) //添加需要拦截的路径
                .excludePathPatterns(exclueUrl); //排除不需要拦截的路径
    }
}
