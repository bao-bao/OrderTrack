package com.ordertrack.pojo;

/* Created by AMXPC on 2018/4/4. */

import com.ordertrack.constant.ReturnCode;

import java.util.List;

public class ListResponse<E> {
    private List<E> list;
    private ReturnCode code;

    public List<E> getList() {
        return list;
    }

    public ListResponse<E> setList(List<E> list) {
        this.list = list;
        return this;
    }

    public ReturnCode getCode() {
        return code;
    }

    public ListResponse<E> setCode(ReturnCode code) {
        this.code = code;
        return this;
    }
}
