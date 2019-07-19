package com.real.name.device.service;

import com.real.name.device.netty.model.TypeId;
import com.real.name.device.netty.annotion.SocketFunction;
import com.real.name.device.netty.annotion.SocketType;
import com.real.name.device.netty.model.AccessFunction;

import javax.persistence.Access;
import java.net.InetSocketAddress;

@SocketType(type = TypeId.ACCESS)
public interface AccessType {

    /**
     * 获取身份证索引号
     */
    @SocketFunction(function = AccessFunction.QUERY_ACCESS_STATUS)
    void queryAccessStatus(Integer deviceId, byte[] data, Integer sequenceId, byte[] externalData, InetSocketAddress address);

    @SocketFunction(function = AccessFunction.QUERY_AUTHORITY)
    void queryAuthority(Integer deviceId, byte[] data, Integer sequenceId, byte[] externalData, InetSocketAddress address);

    @SocketFunction(function = AccessFunction.SEARCH_ACCESS)
    void searchAuthority(Integer deviceId, byte[] data, Integer sequenceId, byte[] externalData, InetSocketAddress address);

    @SocketFunction(function = AccessFunction.CLEAR_AUTHORITY)
    void clearAuthority(Integer deviceId, byte[] data, Integer sequenceId, byte[] externalData, InetSocketAddress address);

}
