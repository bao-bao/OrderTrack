package com.ordertrack.pojo;

/* Created by AMXPC on 2018/4/19. */

import com.ordertrack.entity.WorkRecord;

import java.util.List;

public class AddWorkRecordRequest {
    private List<WorkRecord> list;
    private Integer detailId;

    public List<WorkRecord> getList() {
        return list;
    }

    public AddWorkRecordRequest setList(List<WorkRecord> list) {
        this.list = list;
        return this;
    }

    public Integer getDetailId() {
        return detailId;
    }

    public AddWorkRecordRequest setDetailId(Integer detailId) {
        this.detailId = detailId;
        return this;
    }
}
