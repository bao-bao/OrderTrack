package com.ordertrack.service.impl;

/* Created by AMXPC on 2018/3/26. */

import com.ordertrack.constant.ReturnCode;
import com.ordertrack.entity.User;
import com.ordertrack.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    @Override
    public ReturnCode loginValidate(String username, String password) {
        return null;
    }

    @Override
    public User getUserInfo(String username) {
        return null;
    }
}
