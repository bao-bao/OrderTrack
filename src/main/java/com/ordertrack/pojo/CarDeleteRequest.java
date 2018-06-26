package com.ordertrack.pojo;

/* Created by AMXPC on 2018/5/30. */

public class CarDeleteRequest {
    private Integer orderId;
    private String license;

    public Integer getOrderId() {
        return orderId;
    }

    public CarDeleteRequest setOrderId(Integer orderId) {
        this.orderId = orderId;
        return this;
    }

    public String getLicense() {
        return license;
    }

    public CarDeleteRequest setLicense(String license) {
        this.license = license;
        return this;
    }
}
