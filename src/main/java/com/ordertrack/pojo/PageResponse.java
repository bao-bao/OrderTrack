package com.ordertrack.pojo;

/* Created by AMXPC on 2018/3/31. */

import com.ordertrack.constant.ReturnCode;

import java.util.List;

public class PageResponse<E> {
    private int pageSize;
    private int pageId;
    private List<E> data;
    private ReturnCode code;

    public int getPageSize() {
        return pageSize;
    }

    public PageResponse<E> setPageSize(int pageSize) {
        this.pageSize = pageSize;
        return this;
    }

    public int getPageId() {
        return pageId;
    }

    public PageResponse<E> setPageId(int pageId) {
        this.pageId = pageId;
        return this;
    }

    public List<E> getData() {
        return data;
    }

    public PageResponse<E> setData(List<E> data) {
        this.data = data;
        return this;
    }

    public ReturnCode getCode() {
        return code;
    }

    public PageResponse<E> setCode(ReturnCode code) {
        this.code = code;
        return this;
    }
}
