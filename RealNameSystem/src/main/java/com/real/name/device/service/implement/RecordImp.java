package com.real.name.device.service.implement;

import com.real.name.device.entity.Record;
import com.real.name.device.service.RecordService;
import com.real.name.device.service.repository.RecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class RecordImp implements RecordService {

    @Autowired
    private RecordRepository recordRepository;

    @Override
    public List<Record> findByPersonIdAndTimeBetween(Integer personId, Date beginTime, Date endTime) {
        return recordRepository.findByPersonIdAndTimeBetween(personId, beginTime, endTime);
    }
}
