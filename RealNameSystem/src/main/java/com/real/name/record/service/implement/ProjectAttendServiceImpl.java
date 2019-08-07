package com.real.name.record.service.implement;

import com.real.name.record.query.ProjectAttendQuery;
import com.real.name.record.service.ProjectAttendService;
import com.real.name.record.service.repository.ProjectAttendMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class ProjectAttendServiceImpl implements ProjectAttendService {

    @Autowired
    private ProjectAttendMapper projectAttendMapper;

    @Override
    public List<ProjectAttendQuery> rangeByPersonNum() {
        return projectAttendMapper.rangeByPersonNum();
    }

    @Override
    public List<ProjectAttendQuery> rangeBySumWorkHours(Date startTime, Date endTime) {
        return projectAttendMapper.rangeBySumWorkHours(startTime, endTime);
    }

    @Override
    public List<ProjectAttendQuery> rangeBySumAttendNum(Date startTime, Date endTime) {
        return projectAttendMapper.rangeBySumAttendNum(startTime, endTime);
    }

    @Override
    public List<ProjectAttendQuery> rangeBySumAttendErrNum(Date startTime, Date endTime) {
        return projectAttendMapper.rangeBySumAttendErrNum(startTime, endTime);
    }
}
