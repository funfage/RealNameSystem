package com.real.name.record.service.implement;

import com.real.name.record.entity.GroupAttend;
import com.real.name.record.service.GroupAttendService;
import com.real.name.record.service.repository.GroupAttendMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class GroupAttendServiceImpl implements GroupAttendService {

    @Autowired
    private GroupAttendMapper groupAttendMapper;

    @Override
    public Double countGroupHoursInPeriod(Integer teamSysNo, Date begin, Date end) {
        return groupAttendMapper.countGroupHoursInPeriod(teamSysNo, begin, end);
    }

    @Override
    public List<GroupAttend> getGroupAttendPeriodInfo(Integer teamSysNo, Date begin, Date end) {
        return groupAttendMapper.getGroupAttendPeriodInfo(teamSysNo, begin, end);
    }
}
