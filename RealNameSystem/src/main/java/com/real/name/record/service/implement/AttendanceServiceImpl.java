package com.real.name.record.service.implement;

import com.real.name.record.entity.Attendance;
import com.real.name.record.service.AttendanceService;
import com.real.name.record.service.repository.AttendanceMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class AttendanceServiceImpl implements AttendanceService {

    @Autowired
    private AttendanceMapper attendanceMapper;

    @Override
    public Double countWorkerHours(List<Integer> projectDetailIds, Date begin, Date end) {
        return attendanceMapper.countWorkerHours(projectDetailIds, begin, end);
    }

    @Override
    public List<Attendance> findPeriodAttendByProjectDetailId(Integer projectDetailId, Date begin, Date end) {
        return attendanceMapper.findPeriodAttendByProjectDetailId(projectDetailId, begin, end);
    }

}
