package com.real.name.device.service;

import com.real.name.device.netty.model.TypeId;
import com.real.name.device.netty.annotion.SocketFunction;
import com.real.name.device.netty.annotion.SocketType;
import com.real.name.device.netty.model.AccessFunction;

import java.net.InetSocketAddress;

@SocketType(type = TypeId.ACCESS)
public interface AccessType {

    /**
     * 获取身份证索引号
     */
    @SocketFunction(function = AccessFunction.QUERY_ACCESS_STATUS)
    public void queryAccessStatus(Integer deviceId, byte[] data, Integer sequenceId, byte[] externalData, InetSocketAddress address);

}
