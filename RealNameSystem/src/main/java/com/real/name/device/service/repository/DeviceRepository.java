package com.real.name.device.service.repository;

import com.real.name.device.entity.Device;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DeviceRepository extends JpaRepository<Device, String> {

    List<Device> findByProjectCode(Integer projectId);

    List<Device> findByProjectCodeNotNull();

    //查找该项目的所有门禁读头，devicetype传入为1
    List<Device> findByProjectCodeAndDeviceType(String projectId, Integer deciceType);

    //数据库是否存在此设备
    boolean existsDeviceByDeviceId(String deviceId);

    //根据deviceType查询指定类型的设备
    List<Device> findAllByDeviceType(Integer deciceType);

    Device findDeviceByDeviceId(String deviceId);
}
