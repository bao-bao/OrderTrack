package com.ordertrack.service;

/* Created by AMXPC on 2018/3/26. */

import com.ordertrack.constant.ReturnCode;
import com.ordertrack.entity.User;

import java.util.List;

public interface UserService {
    ReturnCode loginValidate(String username, String password);
    User getUserInfo(String username);
    List<User> getUserList(int pageId, int pageSize, User filter);
    List<User> queryUserList(String username, Integer role);
    ReturnCode addUser(User user);
    ReturnCode updateUser(User user);
    ReturnCode deleteUser(User user);
}
