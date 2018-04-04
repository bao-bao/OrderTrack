package com.ordertrack.pojo;

/* Created by AMXPC on 2018/4/4. */

public class UserListRequest {
    private String name;
    private Integer role;

    public String getName() {
        return name;
    }

    public UserListRequest setName(String name) {
        this.name = name;
        return this;
    }

    public Integer getRole() {
        return role;
    }

    public UserListRequest setRole(Integer role) {
        this.role = role;
        return this;
    }
}
