package com.real.name.device.service;

import com.real.name.device.entity.Record;
import org.apache.ibatis.annotations.Param;

public interface RecordService {

    int saveRecord(Record record);
}
