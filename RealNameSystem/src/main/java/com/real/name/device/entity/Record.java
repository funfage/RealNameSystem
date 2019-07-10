package com.real.name.device.entity;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Record {

    private Integer recordId;

    private String deviceId;

    private Integer deviceType;

    private Integer personId;

    private String personName;

    private Long time;

    private String type;

    private String path;

    private Integer direction;

    private Integer channel;

    public Record() {
    }

    public Record(Integer recordId, String deviceId, Integer deviceType, Integer personId, String personName, Long time, String type, String path, Integer direction, Integer channel) {
        this.recordId = recordId;
        this.deviceId = deviceId;
        this.deviceType = deviceType;
        this.personId = personId;
        this.personName = personName;
        this.time = time;
        this.type = type;
        this.path = path;
        this.direction = direction;
        this.channel = channel;
    }

    public Record(String deviceId, Integer deviceType, Integer personId, String personName, Long time, String type, String path, Integer direction, Integer channel) {
        this.deviceId = deviceId;
        this.deviceType = deviceType;
        this.personId = personId;
        this.personName = personName;
        this.time = time;
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
                ", deviceType='" + deviceType + '\'' +
                ", personId=" + personId +
                ", personName='" + personName + '\'' +
                ", time=" + time +
                ", type='" + type + '\'' +
                ", path='" + path + '\'' +
                ", direction=" + direction +
                ", channel=" + channel +
                '}';
    }
}
