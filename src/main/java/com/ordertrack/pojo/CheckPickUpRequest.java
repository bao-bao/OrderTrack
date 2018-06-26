package com.ordertrack.pojo;

/* Created by AMXPC on 2018/5/4. */

import com.ordertrack.entity.PackLoss;

import java.util.List;

public class CheckPickUpRequest {
    private int orderId;
    private List<PackLoss> loss;

    public int getOrderId() {
        return orderId;
    }

    public CheckPickUpRequest setOrderId(int orderId) {
        this.orderId = orderId;
        return this;
    }

    public List<PackLoss> getLoss() {
        return loss;
    }

    public CheckPickUpRequest setLoss(List<PackLoss> loss) {
        this.loss = loss;
        return this;
    }
}
