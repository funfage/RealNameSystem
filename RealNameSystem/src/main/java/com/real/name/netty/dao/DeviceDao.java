package com.real.name.netty.dao;


import com.real.name.device.entity.Device;
import com.real.name.netty.Entity.Controller;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Mapper
@Component
//@DependsOn("SpringUtil")
public interface DeviceDao {
    //获取数据库所有读头
    @Select("select * from `device` where device_type = 1 OR device_type=2")
    List<Controller> findDutouAll();

    //根据设备ID匹配device
    @Select("select * from `device` where device_id = #{deviceid}")
    Controller getDutouByID(String deviceid);

    //获取某一项目所有的录用读头
    @Select("select * from `device` where device_type=2 and project_code = #{projectCode} ")
    List<String> getController(String projectCode);

    @Select({"<script>",
            "select s.* from `device` as s ",
            "<where>",
            "1=1",
            "<if test='startTime!=null'>",
            "AND install_time &gt;=#{startTime}",
            "</if>",
            "<if test='endTime!=null'>",
            "AND install_time &lt;=#{endTime}",
            "</if>",
            "<if test='deviceId!=null'>",
            "AND device_id =#{deviceId}",
            "</if>",
            "<if test='projectCode!=null'>",
            "AND project_code = #{projectCode}",
            "</if>",
            "order by install_time DESC",
            "limit #{start},#{end}",
            "</where>",
            "</script>"})
    List<Device> getDevice(Map map);

    @Update("UPDATE `device` SET channel= #{channel},direction = #{direction}, " +
            "ip= #{ip},out_port = #{out_port} WHERE project_code = #{projectCode}AND device_id = #{deviceId}")
    Integer updateDevice(Map map);

    @Select({"<script>",
            "select count(*) from `device`  ",
            "<where>",
            "1=1",
            "<if test='startTime!=null'>",
            "AND install_time &gt;=#{startTime}",
            "</if>",
            "<if test='endTime!=null'>",
            "AND install_time &lt;=#{endTime}",
            "</if>",
            "<if test='deviceId!=null'>",
            "AND device_id =#{deviceId}",
            "</if>",
            "<if test='projectCode!=null'>",
            "AND project_code = #{projectCode}",
            "</if>",
            "</where>",
            "</script>"})
    Integer countDevice(Map map);

    /*@Insert({"<script>", "INSERT INTO `device`",
            "<trim>",
            "(",
            "deviceType,",
            "deviceId,",
            "factory,",
            "<if test='phone!=null'>", " phone,", "</if>",
            "<if test='remark!=null'>", " remark,", "</if>",
            ")",
            "</trim>",
            "<trim>",
            "VALUES(",
            "#{deviceType}",
            "#{deviceId}",
            "#{factory}",
            "<if test='phone!=null'>", "#{phone}", "</if>",
            "<if test='remark!=null'>", "#{remark}", "</if>",
            ")",
            "</trim>",
            "</script>"})
    Integer addDevice(Map map);*/


}
