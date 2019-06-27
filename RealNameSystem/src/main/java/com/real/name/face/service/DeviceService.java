package com.real.name.face.service;

import com.real.name.face.entity.Device;

import java.util.List;
import java.util.Optional;

public interface DeviceService {

    /**
     * 查询项目中的设备
     * @param projectId 项目id
     */
    List<Device> findByProjectCode(Integer projectId);

    /**
     * 查找设备
     * @param deviceId 设备id
     */
    Optional<Device> findByDeviceId(String deviceId);

    /**
     * 保存
     */
    Device save(Device device);

    /**
     * 查询全部设备
     */
    List<Device> findAll();

    /**
     * 查询可用设备，未被项目绑定的设备
     */
    List<Device> findAvailableDevice();

    /**
     * 查询某一项目所有的门禁控制器
     */
    List<Device> findDutouOfmenjin(String projectId, Integer deciceType);

    boolean existsDeviceByDeviceId(String deviceId);
}
