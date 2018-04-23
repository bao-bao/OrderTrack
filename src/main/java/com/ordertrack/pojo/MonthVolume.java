package com.ordertrack.pojo;

import javax.persistence.*;

/* Created by AMXPC on 2018/4/23. */
@Entity
public class MonthVolume {
    @Id
    @Column(name = "orderid")
    private Integer id;

    @Column(name = "totalprice")
    private Double volume;

    @Column(name = "month(deliverydate)")
    private Integer month;

    public Double getVolume() {
        return volume;
    }

    public MonthVolume setVolume(Double volume) {
        this.volume = volume;
        return this;
    }

    public Integer getMonth() {
        return month;
    }

    public MonthVolume setMonth(Integer month) {
        this.month = month;
        return this;
    }
}
