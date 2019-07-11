package com.real.name.common.schedule.entity;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PageInfo {

    private Integer index;

    private Integer length;

    private Integer size;

    private Integer total;


    public PageInfo() {
    }

    public PageInfo(Integer index, Integer length, Integer size, Integer total) {
        this.index = index;
        this.length = length;
        this.size = size;
        this.total = total;
    }

    @Override
    public String toString() {
        return "PageInfo{" +
                "index=" + index +
                ", length=" + length +
                ", size=" + size +
                ", total=" + total +
                '}';
    }
}
