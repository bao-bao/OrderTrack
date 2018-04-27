package com.ordertrack.pojo;

/* Created by AMXPC on 2018/4/27. */

import com.ordertrack.constant.ReturnCode;
import com.ordertrack.entity.OrderDetail;

public class OrderDetailIdResponse {
    private OrderDetail detail;
    private ReturnCode code;

    public OrderDetail getDetail() {
        return detail;
    }

    public OrderDetailIdResponse setDetail(OrderDetail detail) {
        this.detail = detail;
        return this;
    }

    public ReturnCode getCode() {
        return code;
    }

    public OrderDetailIdResponse setCode(ReturnCode code) {
        this.code = code;
        return this;
    }
}
