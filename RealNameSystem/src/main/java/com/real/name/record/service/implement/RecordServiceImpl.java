package com.real.name.record.service.implement;

import com.real.name.record.entity.Record;
import com.real.name.record.service.RecordService;
import com.real.name.record.service.repository.RecordMapper;
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
