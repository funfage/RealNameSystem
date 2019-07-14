package com.real.name.device.service;

public interface AccessService {

    /**
     * 给控制器添加权限
     * @param deviceId
     * @param idCardIndex
     */
    void addAuthority(String deviceId, String idCardIndex);

}
