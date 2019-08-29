package com.real.name.device.service.repository;

import com.real.name.device.entity.Device;
import com.real.name.device.query.DeviceQuery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

@Mapper
public interface DeviceQueryMapper {

    /**
     * 搜索设备
     */
    List<Device> searchDevice(@Param("deviceQuery") DeviceQuery deviceQuery);

    /**
     * 搜索项目中的设备
     */
    List<Device> searchDeviceInPro(@Param("deviceQuery") DeviceQuery deviceQuery);

    /**
     * 根据项目id查询设备ip
     */
    Set<String> findIPByProjectCode(@Param("projectCode") String projectCode);

    /**
     * 获取设备总数量
     */
    Integer getFaceDeviceNumber();

}
