package com.ordertrack.pojo;

/* Created by AMXPC on 2018/4/9. */

import com.ordertrack.constant.ReturnCode;
import com.ordertrack.entity.Order;

public class OrderInfoResponse {
    private ReturnCode code;
    private Order order;

    public ReturnCode getCode() {
        return code;
    }

    public OrderInfoResponse setCode(ReturnCode code) {
        this.code = code;
        return this;
    }

    public Order getOrder() {
        return order;
    }

    public OrderInfoResponse setOrder(Order order) {
        this.order = order;
        return this;
    }
}
