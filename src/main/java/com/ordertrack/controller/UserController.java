package com.ordertrack.controller;

/* Created by AMXPC on 2018/3/28. */

import com.ordertrack.constant.ReturnCode;
import com.ordertrack.entity.User;
import com.ordertrack.pojo.ListResponse;
import com.ordertrack.pojo.LoginRequest;
import com.ordertrack.pojo.LoginResponse;
import com.ordertrack.pojo.UserListRequest;
import com.ordertrack.service.impl.UserServiceImpl;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;

@Controller
@RequestMapping("/api/user")
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

    @ResponseBody
    @PostMapping("/getUserList")
    public ListResponse<User> getUserList(@RequestBody UserListRequest filterData) {
        ListResponse<User> resp = new ListResponse<>();
        String username = filterData.getName();
        Integer role = filterData.getRole();
        List<User> userList = userService.queryUserList(username, role);
        resp.setList(userList);
        resp.setCode(ReturnCode.SUCCESS);
        return resp;
    }

    @ResponseBody
    @PostMapping("/addUser")
    public ReturnCode addUser(@RequestBody User user) {
        String username = user.getName();
        if(userService.getUserInfo(username) == null) {
            user.setPassword("123456");
            return userService.addUser(user);
        } else {
            return ReturnCode.USERNAME_EXIST;
        }
    }

    @ResponseBody
    @PostMapping("/updateUser")
    public ReturnCode updateUser(@RequestBody User user) {
        return userService.updateUser(user);
    }

    @ResponseBody
    @PostMapping("/deleteUser")
    public ReturnCode deleteUser(@RequestBody User user) {
        return userService.deleteUser(user);
    }
}