package com.real.name.record;

import com.real.name.device.entity.Record;
import com.real.name.device.service.repository.RecordMapper;
import com.real.name.others.BaseTest;
import com.sun.org.apache.regexp.internal.RE;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class RecordTest extends BaseTest {

    @Autowired
    private RecordMapper mapper;

    @Test
    public void saveRecord() {
        Record record = new Record();
        record.setDeviceId("6668");
        record.setDeviceType(3);
        record.setPersonId(102);
        record.setPersonName("bbb");
        record.setDirection(1);
        record.setChannel(3);
        record.setTime(1562754688060L);
        record.setPath("http://localhost:9901/102.jpg");
        int i = mapper.saveRecord(record);
        System.out.println(i);
    }

    @Test
    public void findAttendancePerson() {
        Record record = mapper.findAttendancePerson(102, "6668", 12345678L);
        System.out.println(record);
    }
}
