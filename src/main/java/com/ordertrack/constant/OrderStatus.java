package com.ordertrack.constant;

/* Created by AMXPC on 2018/4/8. */

public enum OrderStatus {
    NEW(1),
    TAKE(2),
    DISTRIBUTING(3),
    PICKING_UP(4),
    BALANCING(5),
    CAR(6),
    CAR_FEE(7),
    FINISH(8);

    private int status;

    OrderStatus(int status){
        this.status = status;
    }

    public int getStatus() {
        return status;
    }

    public OrderStatus setStatus(int status) {
        this.status = status;
        return this;
    }
}
