package com.example.shopapp.service;



import com.example.shopapp.Util.ServerResponse;
import com.example.shopapp.entity.User;

public interface IUserService {
    public ServerResponse userLogin(String username, String password);
//   int register(String name,String password,String phonenumber,String idcard);
    public ServerResponse Userregister(User user);
    User selectUserByAccountAndPassword(Integer userId);
    public ServerResponse updateUser(User user);
    public ServerResponse alterPsd(User user);
    public ServerResponse updateAvatar(String avatarPath,Integer userId);
    public ServerResponse updateName(Integer userId,String username);
}
