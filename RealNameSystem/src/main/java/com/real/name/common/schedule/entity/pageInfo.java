package com.real.name.common.schedule.entity;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class pageInfo {

    private Integer index;

    private Integer length;

    private Integer size;

    private Integer total;


    public pageInfo() {
    }

    public pageInfo(Integer index, Integer length, Integer size, Integer total) {
        this.index = index;
        this.length = length;
        this.size = size;
        this.total = total;
    }

    @Override
    public String toString() {
        return "pageInfo{" +
                "index=" + index +
                ", length=" + length +
                ", size=" + size +
                ", total=" + total +
                '}';
    }
}
