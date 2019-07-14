package com.real.name.device.netty.model;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AccessRequest {

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
    private byte data[] = new byte[32];

    /**
     * 流水号
     */
    private int sequenceId = 0;

    /**
     * 拓展20字节
     */
    private byte externalData[] = new byte[32];

    public AccessRequest() {
    }

    public static AccessRequest valueOf(byte type, byte functionId, short reserved, int deviceId, byte[] data, int sequenceId, byte[] externalData) {
        AccessRequest accessRequest = new AccessRequest();
        accessRequest.setType(type);
        accessRequest.setFunctionId(functionId);
        accessRequest.setReserved(reserved);
        accessRequest.setDeviceId(deviceId);
        accessRequest.setData(data);
        accessRequest.setSequenceId(sequenceId);
        accessRequest.setExternalData(externalData);
        return accessRequest;
    }

    public static AccessRequest valueOf(byte type, byte functionId, short reserved, int deviceId, byte[] data) {
        AccessRequest accessRequest = new AccessRequest();
        accessRequest.setType(type);
        accessRequest.setFunctionId(functionId);
        accessRequest.setReserved(reserved);
        accessRequest.setDeviceId(deviceId);
        accessRequest.setData(data);
        return accessRequest;
    }

    public AccessRequest(byte type, byte functionId, short reserved, int deviceId, byte[] data) {
        this.type = type;
        this.functionId = functionId;
        this.reserved = reserved;
        this.deviceId = deviceId;
        this.data = data;
    }

    public AccessRequest(byte type, byte functionId, short reserved, int deviceId, byte[] data, int sequenceId, byte[] externalData) {
        this.type = type;
        this.functionId = functionId;
        this.reserved = reserved;
        this.deviceId = deviceId;
        this.data = data;
        this.sequenceId = sequenceId;
        this.externalData = externalData;
    }
}















