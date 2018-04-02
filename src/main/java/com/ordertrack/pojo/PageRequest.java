package com.ordertrack.pojo;

/* Created by AMXPC on 2018/3/31. */


public class PageRequest<E> {
    private int pageSize;
    private int pageId;
    private E filterData;

    public int getPageSize() {
        return pageSize;
    }

    public PageRequest<E> setPageSize(int pageSize) {
        this.pageSize = pageSize;
        return this;
    }

    public int getPageId() {
        return pageId;
    }

    public PageRequest<E> setPageId(int pageId) {
        this.pageId = pageId;
        return this;
    }

    public E getFilterData() {
        return filterData;
    }

    public PageRequest<E> setFilterData(E filterData) {
        this.filterData = filterData;
        return this;
    }
}
