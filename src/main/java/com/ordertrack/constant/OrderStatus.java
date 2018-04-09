package com.ordertrack.constant;

/* Created by AMXPC on 2018/4/8. */

public enum OrderStatus {
    NEW(1),
    PICKING_UP(2),
    DISTRIBUTING(3),
    PACKAGING(4),
    BALANCING(5),
    FINISH(6);

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
