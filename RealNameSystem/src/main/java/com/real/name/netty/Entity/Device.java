package com.real.name.netty.Entity;

import io.netty.channel.ChannelHandlerContext;
import lombok.Data;

import javax.persistence.Entity;


@Data
public class Device {
    private String deviceID;
    private int projectCode;
    private int deviceType;
    private String pass;
    private String ip;
    private int direction;
    private ChannelHandlerContext ctx;
}
