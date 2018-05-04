package com.ordertrack.pojo;

/* Created by AMXPC on 2018/5/4. */

import com.ordertrack.constant.ReturnCode;

public class PackageCheckResponse {
    ReturnCode code;
    String message;

    public ReturnCode getCode() {
        return code;
    }

    public PackageCheckResponse setCode(ReturnCode code) {
        this.code = code;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public PackageCheckResponse setMessage(String message) {
        this.message = message;
        return this;
    }
}
