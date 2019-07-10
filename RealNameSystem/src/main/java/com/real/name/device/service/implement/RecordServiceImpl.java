package com.real.name.device.service.implement;

import com.real.name.device.entity.Record;
import com.real.name.device.service.RecordService;
import com.real.name.device.service.repository.RecordMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RecordServiceImpl implements RecordService {

    @Autowired
    private RecordMapper recordMapper;

    @Override
    public int saveRecord(Record record) {
        return recordMapper.saveRecord(record);
    }

}
