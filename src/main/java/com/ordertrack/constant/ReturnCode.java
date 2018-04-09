package com.ordertrack.constant;

/* Created by AMXPC on 2018/3/28. */

public enum ReturnCode {
    SUCCESS(0),
    // for login module
    INVALID_USERNAME(101),
    WRONG_PASSWORD(102),
    // sql error
    DATABASE_ERROR(201),
    // user el.
    USERNAME_EXIST(301),
    // order el.
    ORDER_INVALID(401)
    ;

    private int code;

    ReturnCode(int code){
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public ReturnCode setCode(int code) {
        this.code = code;
        return this;
    }
}
