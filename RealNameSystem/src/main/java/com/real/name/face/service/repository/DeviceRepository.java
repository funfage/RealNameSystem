package com.real.name.face.service.repository;

import com.real.name.face.entity.Device;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DeviceRepository extends JpaRepository<Device, String> {

    List<Device> findByProjectCode(Integer projectId);

    List<Device> findByProjectCodeNotNull();

    //查找该项目的所有门禁读头，devicetype传入为1
    List<Device> findByProjectCodeAndDeviceType(String projectId, Integer deciceType);
    //数据库是否存在此设备
    boolean existsDeviceByDeviceId(String deviceId);

    Device findDeviceByDeviceId(String deviceId);
}
