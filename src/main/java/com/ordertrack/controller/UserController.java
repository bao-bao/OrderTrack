package com.ordertrack.controller;

/* Created by AMXPC on 2018/3/28. */

import com.ordertrack.constant.ReturnCode;
import com.ordertrack.entity.User;
import com.ordertrack.pojo.LoginRequest;
import com.ordertrack.pojo.LoginResponse;
import com.ordertrack.service.impl.UserServiceImpl;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

@Controller
@RequestMapping("/api")
public class UserController {
    @Resource
    private UserServiceImpl userService;

    @ResponseBody
    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest request) {
        LoginResponse resp = new LoginResponse();
        String username = request.getUsername();
        String password = request.getPassword();
        ReturnCode code = userService.loginValidate(username, password);
        resp.setCode(code);
        if(ReturnCode.SUCCESS == code) {
            User user = userService.getUserInfo(username);
            user.setPassword("");
            resp.setCurrentUser(user);
        }
        return resp;
    }
}
