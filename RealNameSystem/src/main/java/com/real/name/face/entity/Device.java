package com.real.name.face.entity;

import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Entity
@DynamicUpdate
@DynamicInsert
@Data
public class Device {

    public static final String PASS = "12345678";

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

    public Device(String deviceId, String ip) {
        this.deviceId = deviceId;
        this.ip = ip;
    }

    public Device() {
    }
}