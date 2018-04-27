package com.ordertrack.controller;

/* Created by AMXPC on 2018/3/28. */

import com.ordertrack.constant.ReturnCode;
import com.ordertrack.constant.SecurityConstant;
import com.ordertrack.entity.User;
import com.ordertrack.pojo.*;
import com.ordertrack.service.impl.UserServiceImpl;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
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

            RequestAttributes ra = RequestContextHolder.getRequestAttributes();
            ServletRequestAttributes sra = (ServletRequestAttributes)ra;
            HttpServletRequest re = sra.getRequest();
            re.getSession().setAttribute(SecurityConstant.CURRENT_USER_LOGIN.getLabel(), user.getName());
            re.getSession().setAttribute(SecurityConstant.CURRENT_AUTHORITY.getLabel(), user.getRole());
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

    @ResponseBody
    @PostMapping("/updateSelf")
    public ReturnCode selfUpdate(@RequestBody UpdateSelfRequest request) {
        User user = request.getUser();
        String newPassword = request.getNewPassword();
        String username = user.getName();
        String password = request.getOldPassword();
        ReturnCode code = userService.loginValidate(username, password);
        if(ReturnCode.SUCCESS == code) {
            user.setPassword(newPassword);
            return userService.updateUser(user);
        } else {
            return ReturnCode.WRONG_PASSWORD;
        }
    }

}