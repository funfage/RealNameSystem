package com.real.name.common.schedule.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class FaceRecordData {

    private pageInfo pageinfo;

    private List<records> recordsList;

    public FaceRecordData() {
    }

    public FaceRecordData(pageInfo pageinfo, List<records> recordsList) {
        this.pageinfo = pageinfo;
        this.recordsList = recordsList;
    }

    @Override
    public String toString() {
        return "FaceRecordData{" +
                "pageinfo=" + pageinfo +
                ", recordsList=" + recordsList +
                '}';
    }
}
