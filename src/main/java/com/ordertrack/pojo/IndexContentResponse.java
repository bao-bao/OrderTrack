package com.ordertrack.pojo;

/* Created by AMXPC on 2018/4/23. */

import com.ordertrack.constant.ReturnCode;

import java.util.List;

public class IndexContentResponse {
    private int businessCount;
    private int operateCount;
    private int pauseCount;
    private int totalCount;
    private int takeCount;
    private int divisionCount;
    private int pickUpCount;
    private int balanceCount;
    private int carCount;
    private List<OrderEvent> eventList;
    private List<VolumeResponse> volume;
    private ReturnCode code;

    public int getBusinessCount() {
        return businessCount;
    }

    public IndexContentResponse setBusinessCount(int businessCount) {
        this.businessCount = businessCount;
        return this;
    }

    public int getOperateCount() {
        return operateCount;
    }

    public IndexContentResponse setOperateCount(int operateCount) {
        this.operateCount = operateCount;
        return this;
    }

    public int getPauseCount() {
        return pauseCount;
    }

    public IndexContentResponse setPauseCount(int pauseCount) {
        this.pauseCount = pauseCount;
        return this;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public IndexContentResponse setTotalCount(int totalCount) {
        this.totalCount = totalCount;
        return this;
    }

    public int getTakeCount() {
        return takeCount;
    }

    public IndexContentResponse setTakeCount(int takeCount) {
        this.takeCount = takeCount;
        return this;
    }

    public int getDivisionCount() {
        return divisionCount;
    }

    public IndexContentResponse setDivisionCount(int divisionCount) {
        this.divisionCount = divisionCount;
        return this;
    }

    public int getPickUpCount() {
        return pickUpCount;
    }

    public IndexContentResponse setPickUpCount(int pickUpCount) {
        this.pickUpCount = pickUpCount;
        return this;
    }

    public int getBalanceCount() {
        return balanceCount;
    }

    public IndexContentResponse setBalanceCount(int balanceCount) {
        this.balanceCount = balanceCount;
        return this;
    }

    public int getCarCount() {
        return carCount;
    }

    public IndexContentResponse setCarCount(int carCount) {
        this.carCount = carCount;
        return this;
    }

    public List<OrderEvent> getEventList() {
        return eventList;
    }

    public IndexContentResponse setEventList(List<OrderEvent> eventList) {
        this.eventList = eventList;
        return this;
    }

    public List<VolumeResponse> getVolume() {
        return volume;
    }

    public IndexContentResponse setVolume(List<VolumeResponse> volume) {
        this.volume = volume;
        return this;
    }

    public ReturnCode getCode() {
        return code;
    }

    public IndexContentResponse setCode(ReturnCode code) {
        this.code = code;
        return this;
    }
}
