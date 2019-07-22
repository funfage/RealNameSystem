package com.real.name.device.service;

import com.real.name.device.entity.Device;
import com.real.name.device.query.DeviceQuery;
import com.real.name.person.entity.Person;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface DeviceService {

    /**
     * 添加人脸设备
     */
    void addFaceDevice(Device device);

    /**
     * 添加控制器设备
     */
    void addAccessDevice(Device device);


    void updateFaceDevice(Device device);

    void updateAccessDevice(Device device);

    /**
     * 删除设备的人员信息
     */
    void deletePersonInDevice(Device device, Person person);

    /**
     * 删除多个设备人员信息
     */
    void deletePersonInDeviceList(List<Device> deviceList, Person person);

    /**
     * 查询项目中的设备
     */
    List<Device> findByProjectCode(String projectCode);

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
     */
    List<String> getDeviceIdList();

    /**
     * 判断设备的ip和端口是否重发
     */
    Optional<Device> findByIpAndOutPort(String ip, Integer outPort);

    /**
     *
     */
    List<Device> findByProjectCodeIn(List<String> projectCodes);

    /**
     * 查询在某个项目集合中的所有人脸设备
     */
    List<Device> findByProjectCodeInAndDeviceType(List<String> projectCodes, Integer deviceType);

    /**
     * 根据projectCode修改设备的ip
     */
    void updateDeviceIPByProjectCode(String ip, @Param("projectCode") String projectCode);

    List<String> findDeviceIdsByProjectCode(String projectCode);

    List<Device> searchDevice(DeviceQuery deviceQuery);



}
