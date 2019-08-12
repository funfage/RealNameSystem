package com.real.name.record.service.implement;

import com.real.name.common.exception.AttendanceException;
import com.real.name.common.result.ResultError;
import com.real.name.person.entity.Person;
import com.real.name.record.entity.Attendance;
import com.real.name.record.entity.PersonAttend;
import com.real.name.record.query.PeriodTime;
import com.real.name.record.service.PersonAttendService;
import com.real.name.record.service.repository.PersonAttendMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class
PersonAttendServiceImpl implements PersonAttendService {

    private Logger logger = LoggerFactory.getLogger(PersonAttendServiceImpl.class);

    @Autowired
    private PersonAttendMapper personAttendMapper;

    @Transactional
    @Override
    public void deletePersonAttendInOneDay(Integer personId, Date dayStart, Date dayEnd) {
        int i = personAttendMapper.deletePersonAttendInOneDay(personId, dayStart, dayEnd);
        if (i <= 0) {
            logger.error("deletePersonAttendInOneDay操作失败");
            throw new AttendanceException(ResultError.OPERATOR_ERROR);
        }
    }

    @Transactional
    @Override
    public void createPersonAttendInOnDay(Integer personId, List<PeriodTime> periodTimes) {
        for (PeriodTime periodTime : periodTimes) {
            //人员进的记录
            PersonAttend entryAttend = new PersonAttend(new Person(personId), new Date(periodTime.getStartTime()), 1);
            personAttendMapper.savePersonAttend(entryAttend);
            //人员出的记录
            PersonAttend exitAttend = new PersonAttend(new Person(personId), new Date(periodTime.getEndTime()), 1);
            personAttendMapper.savePersonAttend(exitAttend);
        }
    }
}


