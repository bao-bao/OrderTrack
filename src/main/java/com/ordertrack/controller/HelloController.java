package com.ordertrack.controller;

/* Created by AMXPC on 2018/3/23. */
// 测试前后端连通性，仅测试用，已无效

import com.ordertrack.entity.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Deprecated
@Controller
@RequestMapping("/api")
public class HelloController {
    @ResponseBody
    @RequestMapping("/hello")
    public User hello() {
        return new User();
    }
}
