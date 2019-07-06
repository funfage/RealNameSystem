package com.real.name.device.service;

import com.real.name.device.entity.Device;

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

    /**
     * 查找某个设备是否存在
     */
    boolean existsDeviceByDeviceId(String deviceId);

    /**
     * 根据设备类型查询所有与设备
     */
    List<Device> findAllByDeviceType(Integer deviceType);

    /**
     * 根据项目id和设备类型查找某个设备
     */
    List<Device> findByProjectCodeAndDeviceType(String projectId, Integer deciceType);

    /**
     * 获取设备所有的id
     * @return
     */
    List<String> getDeviceIdList();

}
