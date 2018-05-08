package com.ordertrack.pojo;

/* Created by AMXPC on 2018/5/8. */

public class PackageListRequest {
    private Integer status;
    private Integer type;

    public Integer getStatus() {
        return status;
    }

    public PackageListRequest setStatus(Integer status) {
        this.status = status;
        return this;
    }

    public Integer getType() {
        return type;
    }

    public PackageListRequest setType(Integer type) {
        this.type = type;
        return this;
    }
}
