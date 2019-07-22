package com.real.name.device.service.repository;

import com.real.name.device.entity.Device;
import com.real.name.device.query.DeviceQuery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DeviceQueryMapper {

    List<Device> searchDevice(@Param("deviceQuery") DeviceQuery deviceQuery);

}
