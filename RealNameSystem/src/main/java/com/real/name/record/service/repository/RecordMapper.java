package com.real.name.record.service.repository;

import com.real.name.record.entity.Record;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface RecordMapper {


    int saveRecord(Record record);

    Record findAttendancePerson(@Param("personId") Integer personId,
                                @Param("deviceId") String deviceId,
                                @Param("timeNumber") Long timeNumber);

    List<Record> getTodayRecord(@Param("personId") Integer personId,
                                @Param("todayTime") long todayTime,
                                @Param("tomorrowTime") long tomorrowTime);


}
