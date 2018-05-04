package com.ordertrack.pojo;

/* Created by AMXPC on 2018/5/3. */

import com.ordertrack.entity.OrderDetail;

public class OrderDetailBatchRequest {
    private OrderDetail detail;
    private Integer count;

    public OrderDetail getDetail() {
        return detail;
    }

    public OrderDetailBatchRequest setDetail(OrderDetail detail) {
        this.detail = detail;
        return this;
    }

    public Integer getCount() {
        return count;
    }

    public OrderDetailBatchRequest setCount(Integer count) {
        this.count = count;
        return this;
    }
}
