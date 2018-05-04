package com.ordertrack.constant;

/* Created by AMXPC on 2018/3/28. */

public enum ReturnCode {
    SUCCESS(0, ""),
    // for login module
    INVALID_USERNAME(101, ""),
    WRONG_PASSWORD(102, ""),
    // sql error
    DATABASE_ERROR(201, ""),
    NO_DATA(202, ""),
    // user el.
    USERNAME_EXIST(301, ""),
    // order el.
    ORDER_INVALID(401, ""),
    NO_ENOUGH_PACKAGE(402, ""),
    NO_AUTHORITY(500, ""),
    ;

    private String message;
    private int code;

    ReturnCode(int code, String message){
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public ReturnCode setCode(int code) {
        this.code = code;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public ReturnCode setMessage(String message) {
        this.message = message;
        return this;
    }
}
