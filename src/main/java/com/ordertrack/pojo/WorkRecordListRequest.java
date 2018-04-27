package com.ordertrack.pojo;

/* Created by AMXPC on 2018/4/27. */

public class WorkRecordListRequest {
    private String name;
    private Integer year;
    private Integer month;

    public String getName() {
        return name;
    }

    public WorkRecordListRequest setName(String name) {
        this.name = name;
        return this;
    }

    public Integer getYear() {
        return year;
    }

    public WorkRecordListRequest setYear(Integer year) {
        this.year = year;
        return this;
    }

    public Integer getMonth() {
        return month;
    }

    public WorkRecordListRequest setMonth(Integer month) {
        this.month = month;
        return this;
    }
}
