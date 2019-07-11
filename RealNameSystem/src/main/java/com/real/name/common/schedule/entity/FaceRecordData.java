package com.real.name.common.schedule.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class FaceRecordData {

    private PageInfo pageinfo;

    private List<Records> records;

    public FaceRecordData() {
    }

    public FaceRecordData(PageInfo pageinfo, List<Records> records) {
        this.pageinfo = pageinfo;
        this.records = records;
    }

    @Override
    public String toString() {
        return "FaceRecordData{" +
                "pageinfo=" + pageinfo +
                ", recordsList=" + records +
                '}';
    }
}
