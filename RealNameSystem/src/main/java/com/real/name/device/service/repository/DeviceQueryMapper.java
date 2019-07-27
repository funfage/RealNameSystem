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
     * @param deviceQuery
     * @return
     */
    List<Device> searchDevice(@Param("deviceQuery") DeviceQuery deviceQuery);

    /**
     * 根据项目id查询设备ip
     */
    Set<String> findIPByProjectCode(@Param("projectCode") String projectCode);

}
