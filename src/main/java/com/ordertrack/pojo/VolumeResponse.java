package com.ordertrack.pojo;

/* Created by AMXPC on 2018/4/23. */

public class VolumeResponse {
    private String date;
    private Double volume;

    public String getDate() {
        return date;
    }

    public VolumeResponse setDate(String date) {
        this.date = date;
        return this;
    }

    public Double getVolume() {
        return volume;
    }

    public VolumeResponse setVolume(Double volume) {
        this.volume = volume;
        return this;
    }
}
