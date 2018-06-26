package com.ordertrack.pojo;

/* Created by AMXPC on 2018/4/8. */

public class OrderListRequest {
    private Integer carFeeType;
    private String contractId;
    private String customName;
    private Long startDate;
    private Long endDate;

    public Integer getCarFeeType() {
        return carFeeType;
    }

    public OrderListRequest setCarFeeType(Integer carFeeType) {
        this.carFeeType = carFeeType;
        return this;
    }

    public String getContractId() {
        return contractId;
    }

    public OrderListRequest setContractId(String contractId) {
        this.contractId = contractId;
        return this;
    }

    public String getCustomName() {
        return customName;
    }

    public OrderListRequest setCustomName(String customName) {
        this.customName = customName;
        return this;
    }

    public Long getStartDate() {
        return startDate;
    }

    public OrderListRequest setStartDate(Long startDate) {
        this.startDate = startDate;
        return this;
    }

    public Long getEndDate() {
        return endDate;
    }

    public OrderListRequest setEndDate(Long endDate) {
        this.endDate = endDate;
        return this;
    }
}
