package com.ntschy.underground.entity.base;

import lombok.Data;

@Data
public class PageQuery<T> {

    private Integer pageIndex;
    private Integer pageSize;
    private long total;
    private T obj;

    public PageQuery(int pageIndex, int pageSize) {
        this.pageIndex = pageIndex;
        this.pageSize = pageSize;
    }

    public PageQuery(Integer pageIndex, Integer pageSize, long total, T obj) {
        this.pageIndex = pageIndex;
        this.pageSize = pageSize;
        this.total = total;
        this.obj = obj;
    }

    public static Integer startLine(Integer currPage, Integer pageSize) {
        return (currPage - 1) * pageSize + 1;
    }

    public static Integer endLine(Integer currPage, Integer pageSize) {
        return currPage * pageSize;
    }

    @Override
    public String toString() {
        return "PageQuery{" +
                "pageIndex=" + pageIndex +
                ", pageSize=" + pageSize +
                ", total=" + total +
                ", obj=" + obj +
                "}";
    }
}
