package com.ordertrack.controller;

/* Created by AMXPC on 2018/3/25. */
// 主页控制器，使用thymeleaf模板

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {
    @GetMapping({ "/", "/index", "/index.html" })
    public String index() {
        return "index";
    }
}
