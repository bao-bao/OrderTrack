package com.ordertrack.entity;

/* Created by AMXPC on 2018/3/25. */

import lombok.Data;

@Data
public class User {
    private Integer id;

    private String name;

    private String sex;

    private Integer age;

    private String role;
}