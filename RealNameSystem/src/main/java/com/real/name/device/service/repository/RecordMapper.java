package com.real.name.device.service.repository;

import com.real.name.device.entity.Record;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface RecordMapper {


    int saveRecord(Record record);

    Record findAttendancePerson(@Param("personId") Integer personId,
                                @Param("deviceId") String deviceId,
                                @Param("time") Long time);

}
