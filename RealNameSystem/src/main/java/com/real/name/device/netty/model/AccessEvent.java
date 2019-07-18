package com.real.name.device.netty.model;

import lombok.Getter;
import lombok.Setter;

import java.net.InetSocketAddress;
import java.util.Arrays;

@Setter
@Getter
public class AccessEvent {

    /**
     * 类型
     */
    private byte type = TypeId.ACCESS;

    /**
     * 功能号
     */
    private byte functionId;

    /**
     * 保留
     */
    private short reserved = 0x00;

    /**
     * 设备序列号,4个字节
     */
    private int deviceId = 0;

    /**
     * 32字节的数据
     */
    private byte data[] = new byte[AccessConstant.DATA_LENGTH];

    /**
     * 流水号
     */
    private int sequenceId = 0;

    /**
     * 拓展20字节
     */
    private byte externalData[] = new byte[AccessConstant.EXTERNAL_DATA_LENGTH];

    /**
     * 设备的IP和端口套接字
     */
    private InetSocketAddress address;

    public AccessEvent() {
    }

    public static AccessEvent valueOf(byte type, byte functionId, short reserved, int deviceId, byte[] data, int sequenceId, byte[] externalData, InetSocketAddress address) {
        AccessEvent accessRequest = new AccessEvent();
        accessRequest.setType(type);
        accessRequest.setFunctionId(functionId);
        accessRequest.setReserved(reserved);
        accessRequest.setDeviceId(deviceId);
        accessRequest.setData(data);
        accessRequest.setSequenceId(sequenceId);
        accessRequest.setExternalData(externalData);
        accessRequest.setAddress(address);
        return accessRequest;
    }

    public static AccessEvent valueOf(byte type, byte functionId, short reserved, int deviceId, byte[] data, InetSocketAddress address) {
        AccessEvent accessRequest = new AccessEvent();
        accessRequest.setType(type);
        accessRequest.setFunctionId(functionId);
        accessRequest.setReserved(reserved);
        accessRequest.setDeviceId(deviceId);
        accessRequest.setData(data);
        accessRequest.setAddress(address);
        return accessRequest;
    }

    public AccessEvent(byte type, byte functionId, short reserved, int deviceId, byte[] data) {
        this.type = type;
        this.functionId = functionId;
        this.reserved = reserved;
        this.deviceId = deviceId;
        this.data = data;
    }

    public AccessEvent(byte type, byte functionId, short reserved, int deviceId, byte[] data, int sequenceId, byte[] externalData) {
        this.type = type;
        this.functionId = functionId;
        this.reserved = reserved;
        this.deviceId = deviceId;
        this.data = data;
        this.sequenceId = sequenceId;
        this.externalData = externalData;
    }

    @Override
    public String toString() {
        return "AccessEvent{" +
                "type=" + type +
                ", functionId=" + functionId +
                ", reserved=" + reserved +
                ", deviceId=" + deviceId +
                ", data=" + Arrays.toString(data) +
                ", sequenceId=" + sequenceId +
                ", externalData=" + Arrays.toString(externalData) +
                ", address=" + address +
                '}';
    }
}















