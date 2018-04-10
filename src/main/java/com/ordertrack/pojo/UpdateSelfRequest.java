package com.ordertrack.pojo;

/* Created by AMXPC on 2018/4/10. */

import com.ordertrack.entity.User;

public class UpdateSelfRequest {
    private String oldPassword;
    private String newPassword;
    private User user;

    public String getOldPassword() {
        return oldPassword;
    }

    public UpdateSelfRequest setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
        return this;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public UpdateSelfRequest setNewPassword(String newPassword) {
        this.newPassword = newPassword;
        return this;
    }

    public User getUser() {
        return user;
    }

    public UpdateSelfRequest setUser(User user) {
        this.user = user;
        return this;
    }
}
