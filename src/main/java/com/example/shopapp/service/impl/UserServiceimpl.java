package com.example.shopapp.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.example.shopapp.Util.ResponseCode;
import com.example.shopapp.Util.ServerResponse;
import com.example.shopapp.entity.User;
import com.example.shopapp.dao.UserMapper;
import com.example.shopapp.entity.UserVo;
import com.example.shopapp.service.IUserService;
import freemarker.template.utility.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class UserServiceimpl  implements IUserService {
    @Autowired
    UserMapper userMapper;
//    @Override
//    public User login(String name, String password) {
//        QueryWrapper<User> qw=new QueryWrapper<>();
//        qw.eq("name",name);
//        qw.eq("password",password);
//        User user =userMapper.selectOne(qw);
//        return user;
//    }

    @Override
    public ServerResponse userLogin(String username, String password) {
        if (StringUtils.isBlank(username)){
            return ServerResponse.createServerResponseByFail(ResponseCode.USERNAME_NOT_EMPTY.getCode(),ResponseCode.USERNAME_NOT_EMPTY.getMsg());
        }
        if (StringUtils.isBlank(password)){
            return ServerResponse.createServerResponseByFail(ResponseCode.PASSWORD_NOT_EMPTY.getCode(),ResponseCode.PASSWORD_NOT_EMPTY.getMsg());
        }
        QueryWrapper<User> wrapper =new QueryWrapper();
        wrapper.eq("username",username);
        Long count =userMapper.selectCount(wrapper);
        if (count==0){
            return ServerResponse.createServerResponseByFail(ResponseCode.USERNAME_NOT_EXISTS.getCode(),ResponseCode.USERNAME_NOT_EXISTS.getMsg());
        }
        User user=userMapper.selectUserPsd(username,password);
        if (user==null){
            return ServerResponse.createServerResponseByFail(ResponseCode.PASSWORD_ERROR.getCode(),ResponseCode.PASSWORD_ERROR.getMsg());
        }
        return ServerResponse.createBySuccess(convert(user));
    }
    private UserVo convert(User user){
        UserVo userVO=new UserVo();
        userVO.setId(user.getId());
        userVO.setUsername(user.getUsername());
        userVO.setIdcard(user.getIdcard());
        userVO.setAvatar(user.getAvatar());
        userVO.setQuestion(user.getQuestion());
        userVO.setAnswer(user.getAnswer());
        userVO.setPassword(user.getPassword());
        userVO.setPhonenumber(user.getPhonenumber());
        userVO.setCreateTime(new Date());
        userVO.setUpdateTime(new Date());

        return userVO;
    }

//    @Override
//    public int register(String name, String password, String phonenumber, String idcard) {
//        User user= new User();
//        user.setName(name);
//        user.setPassword(password);
//        user.setIdcard(idcard);
//        user.setPhonenumber(phonenumber);
//        return  userMapper.insert(user);
//    }




    @Override
    public ServerResponse updateUser(User user) {
        User user1=new User();
        user1.setQuestion(user.getQuestion());
        user1.setUpdateTime(new Date());
        user1.setIdcard(user.getIdcard());
        user1.setAnswer(user.getAnswer());
        user1.setPhonenumber(user.getPhonenumber());
        user1.setUsername(user.getUsername());
        user1.setPassword(user.getPassword());
        int count=userMapper.updateById(user);
        if (count==0){
            return ServerResponse.createServerResponseByFail(ResponseCode.USERINFO_UPDATE_FAIL.getCode(),ResponseCode.USERINFO_UPDATE_FAIL.getMsg());
        }
        return ServerResponse.createBySuccess();
    }

    @Override
    public ServerResponse alterPsd(User user) {
        String question=user.getQuestion();
        String password=user.getPassword();
        String answer=user.getAnswer();
        String idcart=user.getIdcard();
        if(idcart==null||idcart.equals("")){
            //
            return ServerResponse.createServerResponseByFail(ResponseCode.EMAIL_NOT_EMPTY.getCode(),ResponseCode.EMAIL_NOT_EMPTY.getMsg());
        }




        if(question==null||question.equals("")){
            //
            return ServerResponse.createServerResponseByFail(ResponseCode.QUESTION_NOT_EMPTY.getCode(),ResponseCode.QUESTION_NOT_EMPTY.getMsg());
        }

        if(answer==null||answer.equals("")){
            //
            return ServerResponse.createServerResponseByFail(ResponseCode.ANSWER_NOT_EMPTY.getCode(),ResponseCode.ANSWER_NOT_EMPTY.getMsg());
        }
        if(password==null||password.equals("")){
            //
            return ServerResponse.createServerResponseByFail(ResponseCode.PASSWORD_NOT_EMPTY.getCode(),ResponseCode.PASSWORD_NOT_EMPTY.getMsg());
        }
        QueryWrapper<User> wrapper=new QueryWrapper<>();
        wrapper.eq("idcard",user.getIdcard()).eq("question",user.getQuestion()).eq("answer",user.getAnswer());
        User user1=new User();
        int result= Math.toIntExact(userMapper.selectCount(wrapper));
        if (result==0){
            return ServerResponse.createServerResponseByFail(ResponseCode.ALTER_ERROR.getCode(),ResponseCode.ALTER_ERROR.getMsg());
        }
        user1.setPassword(password);
        UpdateWrapper updateWrapper=new UpdateWrapper();
        updateWrapper.eq("idcard",idcart);
        int count=userMapper.update(user1,wrapper);
        if (count>0){
            return ServerResponse.createBySuccessMessage("修改密码成功");
        }
        return ServerResponse.createBySuccess();
    }

    @Override
    public ServerResponse updateAvatar(String avatarPath, Integer userId) {
        UpdateWrapper<User> wrapper=new UpdateWrapper<>();
        wrapper.eq("id",userId);
        User user=new User();
        user.setAvatar(avatarPath);
        int i=userMapper.update(user,wrapper);
        if (i==0){
           return ServerResponse.createByErrorMessage("上传失败");
        }
        User newuser=userMapper.selectById(userId);
        UserVo userVo=convert(newuser);
        return  ServerResponse.createBySuccess(userVo);
    }

    @Override
    public ServerResponse updateName(Integer userId,String username) {
        UpdateWrapper<User> wrapper=new UpdateWrapper<>();
        wrapper.eq("id",userId);
        User user =new User();
        user.setUsername(username);
        int count =userMapper.update(user,wrapper);
        if (count==0){
            return ServerResponse.createByErrorMessage("修改名字失败");
        }
        User newuser=userMapper.selectById(userId);
        UserVo userVo=convert(newuser);
        return  ServerResponse.createBySuccess(userVo);
    }

    @Override
    public User selectUserByAccountAndPassword(Integer userId) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", userId);
        User user = userMapper.selectOne(queryWrapper);
        return user;
    }

    @Override
    public ServerResponse Userregister(User user) {
        if(user==null){
            return ServerResponse.createServerResponseByFail(ResponseCode.PARAMTER_NOT_EMPTY.getCode(),ResponseCode.PARAMTER_NOT_EMPTY.getMsg());
        }
        String username=user.getUsername();
        String password=user.getPassword();
        String phone=user.getPhonenumber();
        String question=user.getQuestion();
        String answer=user.getAnswer();
        String idcart=user.getIdcard();
        if(username==null||username.equals("")){
            //
            return ServerResponse.createServerResponseByFail(ResponseCode.USERNAME_NOT_EMPTY.getCode(),ResponseCode.USERNAME_NOT_EMPTY.getMsg());
        }
        if(password==null||password.equals("")){
            //
            return ServerResponse.createServerResponseByFail(ResponseCode.PASSWORD_NOT_EMPTY.getCode(),ResponseCode.PASSWORD_NOT_EMPTY.getMsg());
        }


        if(idcart==null||idcart.equals("")){
            //
            return ServerResponse.createServerResponseByFail(ResponseCode.EMAIL_NOT_EMPTY.getCode(),ResponseCode.EMAIL_NOT_EMPTY.getMsg());
        }

        if(question==null||question.equals("")){
            //
            return ServerResponse.createServerResponseByFail(ResponseCode.QUESTION_NOT_EMPTY.getCode(),ResponseCode.QUESTION_NOT_EMPTY.getMsg());
        }

        if(answer==null||answer.equals("")){
            //
            return ServerResponse.createServerResponseByFail(ResponseCode.ANSWER_NOT_EMPTY.getCode(),ResponseCode.ANSWER_NOT_EMPTY.getMsg());
        }

        if(phone==null||phone.equals("")){
            //
            return ServerResponse.createServerResponseByFail(ResponseCode.PHONE_NOT_EMPTY.getCode(),ResponseCode.PHONE_NOT_EMPTY.getMsg());
        }
        QueryWrapper<User> wrapper=new QueryWrapper<>();
        wrapper.eq("username",username);
        Long i=userMapper.selectCount(wrapper);
        if (i>0){
            return ServerResponse.createServerResponseByFail(ResponseCode.USERNAME_EXISTS.getCode(),ResponseCode.USERNAME_EXISTS.getMsg());
        }
        user.setPassword(user.getPassword());
        user.setCreateTime(new Date());
        user.setPhonenumber(user.getPhonenumber());
        user.setAnswer(user.getAnswer());
        user.setIdcard(user.getIdcard());
        user.setQuestion(user.getQuestion());
        Integer result=userMapper.insert(user);
        if (result==0){
            return ServerResponse.createServerResponseByFail(ResponseCode.REGISTER_FAIL.getCode(),ResponseCode.REGISTER_FAIL.getMsg());
        }
        return ServerResponse.createBySuccessMessage("注册成功");
    }

}

