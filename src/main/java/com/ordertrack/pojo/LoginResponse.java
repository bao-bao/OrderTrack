package com.ordertrack.pojo;

/* Created by AMXPC on 2018/3/28. */

import com.ordertrack.constant.ReturnCode;
import com.ordertrack.entity.User;

public class LoginResponse {
    private User currentUser;
    private ReturnCode code;

    public User getCurrentUser() {
        return currentUser;
    }

    public LoginResponse setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
        return this;
    }

    public ReturnCode getCode() {
        return code;
    }

    public LoginResponse setCode(ReturnCode code) {
        this.code = code;
        return this;
    }
}
