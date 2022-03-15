package com.example.shopapp.controller;

import com.example.shopapp.Util.Const;
import com.example.shopapp.Util.ResponseCode;
import com.example.shopapp.Util.ServerResponse;
import com.example.shopapp.entity.BaseModel;
import com.example.shopapp.entity.User;
import com.example.shopapp.entity.UserVo;
import com.example.shopapp.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;

@Controller
@RequestMapping("/user")
public class UserController {
    @Autowired
    IUserService iUserService;
@ResponseBody
@RequestMapping("login")
public ServerResponse login(String username, String password, HttpSession session){
    ServerResponse serverResponse=iUserService.userLogin(username,password);
    if (serverResponse.getStatus()==200){
            session.setAttribute(Const.LOGIN_SUCCESS,serverResponse.getData());
    }
    return serverResponse;
}
//  public BaseModel login(@RequestBody User user){
//      User user1 =iUserService.login(user.getName(),user.getPassword());
//      BaseModel model;
//      if (user1 == null) {
//          model = BaseModel.createRespose(201, "登录失败");
//
//      }else {
//          model =  BaseModel.createRespose(200, "登录成功");
//
//          model.setData(user1.getId());
//      }
//      return model;
//  }

    @ResponseBody
    @RequestMapping("register")
    public ServerResponse Register( @RequestBody User user){
      return iUserService.Userregister(user);
    }
    @ResponseBody
    @RequestMapping("getUserInfo")
    public  BaseModel getUserInfo(int userId){
        User user=  iUserService.selectUserByAccountAndPassword(userId);
        BaseModel<User> baseModel =new BaseModel<>();
        if(user==null){
            baseModel.setCode(201);
            baseModel.setMsg("获取用户信息失败");
        }else {
            baseModel.setCode(200);
            baseModel.setMsg("获取用户信息成功");
            baseModel.setData(user);
        }
        return  baseModel;
    }
    @ResponseBody
    @RequestMapping("updateUserInfo")
    public  ServerResponse updateUser(User user,HttpSession session){
        UserVo userInfo= (UserVo) session.getAttribute(Const.LOGIN_SUCCESS);

        user.setId(userInfo.getId());
        ServerResponse serverResponse=iUserService.updateUser(user);
        if (serverResponse.getStatus()==200){
            session.setAttribute(Const.LOGIN_SUCCESS,serverResponse.getData());
        }
        return serverResponse;
    }


    @ResponseBody
    @RequestMapping("alterPassword")
    public  ServerResponse alterPsd(@RequestBody User user){


        ServerResponse serverResponse=iUserService.alterPsd(user);

        return serverResponse;
    }
    @ResponseBody
    @RequestMapping("loginout")
    public  ServerResponse logout(HttpSession session){

        session.removeAttribute(Const.LOGIN_SUCCESS);
        return ServerResponse.createServerResponseBySucess();

    }
    @ResponseBody
    @RequestMapping("upload")
    public ServerResponse uploadFile( MultipartFile file , HttpSession session){
        UserVo userVO=(UserVo) session.getAttribute(Const.LOGIN_SUCCESS);
        //指定存储路径
        String dirPath ="E:"+ File.separator + "upload";//File.separator相当于/

        File dir=new File(dirPath);
        if(!dir.exists()){
            dir.mkdirs();//生成该目录
        }
        String fileName=System.currentTimeMillis()+file.getOriginalFilename();//获取原始文件名称，System.currentTimeMillis()保证重名文件不被覆盖
        File file1=new File(dir,fileName);//生成dir为文件目录 fileName为文件名，在该文件夹下生成一个和上传文件同名的文件
        BaseModel baseModel;
        try {
            file.transferTo(file1);//上传文件
        } catch (IOException e) {
            e.printStackTrace();
            return  ServerResponse.createByErrorMessage("上传失败");
        }
        ServerResponse serverResponse=  iUserService.updateAvatar(fileName,userVO.getId());
        if (serverResponse.isSuccess()){
            session.setAttribute(Const.LOGIN_SUCCESS,serverResponse.getData());
        }
        return   serverResponse;
    }
    @ResponseBody
    @RequestMapping("updateName")
    public ServerResponse updateName(HttpSession session,String username){
        UserVo userVO=(UserVo) session.getAttribute(Const.LOGIN_SUCCESS);
        ServerResponse serverResponse=iUserService.updateName(userVO.getId(),username);
        if (serverResponse.isSuccess()){
            session.setAttribute(Const.LOGIN_SUCCESS,serverResponse.getData());
        }
        return serverResponse;
    }
}
