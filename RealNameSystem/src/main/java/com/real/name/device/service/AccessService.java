package com.real.name.device.service;

public interface AccessService {

    /**
     * 给控制器添加权限
     */
    void addAuthority(String deviceId, String idCardIndex, String ip, int port);

    /**
     * 查询权限
     */
    void queryAuthority(String deviceId, String idCardIndex, String ip, int port);

    /**
     * 搜索控制器
     */
    void searchAccess(String deviceId, String ip, int port);

    /**
     * 清空权限
     */
    void clearAccess(String deviceId, String ip, int port);

    /**
     * 删除权限
     */
    void deleteAuthority(String deviceId, String idCardIndex, String ip, int port);

}
