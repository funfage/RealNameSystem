package com.real.name.record.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
public class Record {

    private Integer recordId;

    private String deviceId;

    private Integer deviceType;

    private Integer personId;

    private String personName;

    private Long timeNumber;

    private Date detailTime;

    private Integer type;

    private String path;

    private Integer direction;

    private Integer channel;

    public Record() {
    }

    public Record(Integer recordId, String deviceId, Integer deviceType, Integer personId, String personName, Long timeNumber, Date detailTime, Integer type, String path, Integer direction, Integer channel) {
        this.recordId = recordId;
        this.deviceId = deviceId;
        this.deviceType = deviceType;
        this.personId = personId;
        this.personName = personName;
        this.timeNumber = timeNumber;
        this.detailTime = detailTime;
        this.type = type;
        this.path = path;
        this.direction = direction;
        this.channel = channel;
    }

    public Record(String deviceId, Integer deviceType, Integer personId, String personName, Long timeNumber, Date detailTime, Integer type, String path, Integer direction, Integer channel) {
        this.deviceId = deviceId;
        this.deviceType = deviceType;
        this.personId = personId;
        this.personName = personName;
        this.timeNumber = timeNumber;
        this.detailTime = detailTime;
        this.type = type;
        this.path = path;
        this.direction = direction;
        this.channel = channel;
    }

    @Override
    public String toString() {
        return "Record{" +
                "recordId=" + recordId +
                ", deviceId='" + deviceId + '\'' +
                ", deviceType=" + deviceType +
                ", personId=" + personId +
                ", personName='" + personName + '\'' +
                ", timeNumber=" + timeNumber +
                ", detailTime=" + detailTime +
                ", type=" + type +
                ", path='" + path + '\'' +
                ", direction=" + direction +
                ", channel=" + channel +
                '}';
    }
}
