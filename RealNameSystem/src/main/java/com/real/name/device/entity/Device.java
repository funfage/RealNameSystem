package com.real.name.device.entity;

import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

@Entity
@DynamicUpdate
@DynamicInsert
@Data
public class Device {

    @Id
    private String deviceId;

    private String projectCode;

    private Integer deviceType;

    private String pass;

    private String ip;

    private Integer direction;

    private Integer channel;

    private Integer outPort;

    private Date installTime;

    private  String factory;

    private String phone;

    private String remark;

    public Device(String deviceId, Integer outPort, String ip, String pass) {
        this.deviceId = deviceId;
        this.outPort = outPort;
        this.ip = ip;
        this.pass = pass;
    }

    public Device() {
    }

    public Device(String deviceId) {
        this.deviceId = deviceId;
    }
}