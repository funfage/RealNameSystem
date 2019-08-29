package com.real.name.device.service;

import com.real.name.device.entity.Device;
import com.real.name.device.query.DeviceQuery;
import com.real.name.person.entity.Person;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public interface DeviceService {


    /**
     * 下发人员信息到设备
     */
    void addPersonToDevices(String projectCode, Person person, List<Device> deviceList);

    /**
     * 添加人脸设备
     */
    void addFaceDevice(Device device);

    /**
     * 添加控制器设备
     */
    void addAccessDevice(Device device);

    /**
     * 更新人脸设备信息
     */
    void updateFaceDevice(Device device);

    /**
     * 更新控制器设备信息
     */
    void updateAccessDevice(Device device);

    /**
     * 删除设备
     */
    void deleteDevice(Device device);

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
     * 查询所有需要下发的设备
     */
    List<Device> findAllIssueDevice();

    /**
     * 查询项目绑定的需要下发的设备
     */
    List<Device> findAllProjectIssueDevice(String projectCode);

    /**
     * 根据设备类型查询所有与设备
     */
    List<Device> findAllByDeviceType(Integer deviceType);

    /**
     * 根据项目id和设备类型查找某个设备
     */
    List<Device> findByProjectCodeAndDeviceType(String projectId, Integer deviceType);

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

    /**
     * 获取项目绑定的所有设备的id
     */
    List<String> findDeviceIdsByProjectCode(String projectCode);

    /**
     * 搜索设备
     */
    List<Device> searchDevice(DeviceQuery deviceQuery);

    /**
     * 搜索项目中的设备
     */
    List<Device> searchDeviceInPro(DeviceQuery deviceQuery);

    /**
     * 根据项目id查询设备ip
     */
    Set<String> findIPByProjectCode(String projectCode);

    /**
     * 获取首页数据
     */
    Map<String, Object> getMainPageDeviceInfo();


}
