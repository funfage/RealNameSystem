package com.real.name.record;

import com.real.name.common.utils.CommonUtils;
import com.real.name.common.utils.TimeUtil;
import com.real.name.project.entity.ProjectDetail;
import com.real.name.project.entity.ProjectDetailQuery;
import com.real.name.project.service.ProjectDetailService;
import com.real.name.record.service.repository.RecordMapper;
import com.real.name.others.BaseTest;
import com.real.name.record.entity.Record;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;

public class RecordTest extends BaseTest {

    @Autowired
    private RecordMapper mapper;

    @Autowired
    private ProjectDetailService projectDetailService;

    @Autowired
    private RecordMapper recordMapper;

    @Test
    public void saveRecord() {
        Record record = new Record();
        record.setDeviceId("6668");
        record.setDeviceType(3);
        record.setPersonId(102);
        record.setPersonName("bbb");
        record.setDirection(1);
        record.setChannel(3);
        record.setTimeNumber(1562754688060L);
        record.setDetailTime(new Date(1562754688060L));
        record.setPath("http://localhost:9901/102.jpg");
        int i = mapper.saveRecord(record);
        System.out.println(i);
    }

    @Test
    public void findAttendancePerson() {
        Record record = mapper.findAttendancePerson(102, "6668", 1562754688060L);
        System.out.println(record);
    }

    @Test
    public void getTodayRecord() {
        List<Record> todayRecord = mapper.getTodayRecord(99, TimeUtil.getTodayBeginMilliSecond(), TimeUtil.getTomorrowBeginMilliSecond());
        System.out.println(todayRecord);

    }

    @Test
    public void countWorkTime() {
        List<ProjectDetail> projectDetailList = projectDetailService.findAll();
        for (ProjectDetail projectDetail : projectDetailList) {
            Integer personId = projectDetail.getPersonId();
            long todayBegin = TimeUtil.getTodayBeginMilliSecond()- 86400000;
            long tomorrowBegin = TimeUtil.getTomorrowBeginMilliSecond()- 86400000;
            //查询该员工的当天的所有进出记录
            List<Record> todayRecord = recordMapper.getTodayRecord(personId, todayBegin, tomorrowBegin);
            long totalTime = 0;
            for (int i = 0; i < todayRecord.size() - 1; i++) {
                int direction1 = todayRecord.get(i).getDirection();
                long time1 = todayRecord.get(i).getTimeNumber();
                int direction2 = todayRecord.get(i + 1).getDirection();
                if (direction1 == 1 && direction2 == 2) {
                    while (++i < todayRecord.size() && todayRecord.get(i).getDirection() == 2) {

                    }
                    if (i == todayRecord.size()) {
                        long time2 = todayRecord.get(i - 1).getTimeNumber();
                        totalTime += time2 - time1;
                    } else {
                        long time2 = todayRecord.get(--i).getTimeNumber();
                        totalTime += time2 - time1;
                    }
                }
            }
            System.out.println(totalTime);
        }
    }
}
