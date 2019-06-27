package com.real.name.face.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

@Entity
@Data
public class Record {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer recordId;

    private String ip;

    private String deviceKey;

    private Integer personId;

    private Date time;

    private String type;

    private String path;

    private Integer direction;

    public Record(String ip, String deviceKey, Integer personId, Date time, String type, String path) {
        this.ip = ip;
        this.deviceKey = deviceKey;
        this.personId = personId;
        this.time = time;
        this.type = type;
        this.path = path;
    }
    public Record(String ip, String deviceKey, Integer personId, Date time, String type, int direction) {
        this.ip = ip;
        this.deviceKey = deviceKey;
        this.personId = personId;
        this.time = time;
        this.type = type;
        this.direction = direction;
    }

    public Record() {
    }
}
