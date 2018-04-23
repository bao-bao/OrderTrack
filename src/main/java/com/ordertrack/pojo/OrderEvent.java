package com.ordertrack.pojo;

/* Created by AMXPC on 2018/4/23. */

public class OrderEvent {
    private String date;
    private String title;
    private String desc;

    public String getDate() {
        return date;
    }

    public OrderEvent setDate(String date) {
        this.date = date;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public OrderEvent setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getDesc() {
        return desc;
    }

    public OrderEvent setDesc(String desc) {
        this.desc = desc;
        return this;
    }
}
