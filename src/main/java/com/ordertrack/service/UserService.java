package com.ordertrack.service;

/* Created by AMXPC on 2018/3/26. */

import com.ordertrack.constant.ReturnCode;
import com.ordertrack.entity.User;

import java.util.List;

public interface UserService {
    ReturnCode loginValidate(String username, String password);
    User getUserInfo(String username);
    List<User> getUserList(int pageId, int pageSize, User filter);
}
