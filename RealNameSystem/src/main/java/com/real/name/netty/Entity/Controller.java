package com.real.name.netty.Entity;

import io.netty.channel.ChannelHandlerContext;
import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;


@Data
public class Controller {
    @Id
    private String deviceId;

    private String projectCode;
    private int deviceType;
    private String pass;
    private String ip;
    private int direction;
    private String cardNo;
    private  Integer outPort;

    //@Transient
    private ChannelHandlerContext ctx;

}
