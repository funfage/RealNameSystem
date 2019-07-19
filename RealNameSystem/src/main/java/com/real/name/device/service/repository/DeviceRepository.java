package com.real.name.device.service.repository;

import com.real.name.device.entity.Device;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface DeviceRepository extends JpaRepository<Device, String> {

    List<Device> findByProjectCode(String projectCode);

    List<Device> findByProjectCodeNotNull();

    //查找该项目的所有门禁读头，deviceType传入为1
    List<Device> findByProjectCodeAndDeviceType(String projectId, Integer deviceType);

    //数据库是否存在此设备
    boolean existsDeviceByDeviceId(String deviceId);

    //根据deviceType查询指定类型的设备
    List<Device> findAllByDeviceType(Integer deciceType);

    @Query("select d.deviceId from Device d")
    List<String> getDeviceIdList();

    //根据ip和端口查询某个设备
    Optional<Device> findByIpAndOutPort(String ip, Integer outPort);

    //查询在projectCodes集合中的所有设备
    List<Device> findByProjectCodeIn(List<String> projectCodes);

    //查询在projectCodes集合中的所有人脸设备
    List<Device> findByProjectCodeInAndDeviceType(List<String> projectCodes, Integer deviceType);


    @Query("update com.real.name.device.entity.Device d set d.ip = :ip where d.projectCode = :projectCode")
    @Modifying
    @Transactional
    void updateDeviceIPByProjectCode(@Param("ip") String ip, @Param("projectCode") String projectCode);

    Optional<Device> findByDeviceId(String deviceId);
}
