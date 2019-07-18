package com.real.name.record;

import com.real.name.others.BaseTest;
import com.real.name.project.entity.ProjectDetailQuery;
import com.real.name.record.entity.Attendance;
import com.real.name.record.service.repository.AttendanceMapper;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

public class AttendanceTest extends BaseTest {

    @Autowired
    private AttendanceMapper attendanceMapper;

    @Test
    public void saveAttendance() {
        Attendance attendance = new Attendance();
        ProjectDetailQuery query = new ProjectDetailQuery();
        query.setId(8);
        attendance.setWorkHours(12349839L);
        attendance.setWorkTime(new Date());
        attendanceMapper.saveAttendance(attendance);
    }


}
