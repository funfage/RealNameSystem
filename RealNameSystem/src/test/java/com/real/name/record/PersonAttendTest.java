package com.real.name.record;

import com.real.name.common.utils.TimeUtil;
import com.real.name.others.BaseTest;
import com.real.name.person.entity.Person;
import com.real.name.record.entity.PersonAttend;
import com.real.name.record.service.repository.PersonAttendMapper;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

public class PersonAttendTest extends BaseTest {

    @Autowired
    private PersonAttendMapper personAttendMapper;

    @Test
    public void savePersonAttend() {
        PersonAttend personAttend = new PersonAttend();
        Person person = new Person();
        person.setPersonId(104);
        personAttend.setPerson(person);
        personAttend.setDirection(2);
        personAttend.setEntryExitTime(new Date());
        int i = personAttendMapper.savePersonAttend(personAttend);
        System.out.println(i);
    }

    @Test
    public void deletePersonAttendInOneDay() {
        Date dateBegin = TimeUtil.getDateBegin(new Date());
        Date dateEnd = TimeUtil.getDateEnd(new Date());
        int i = personAttendMapper.deletePersonAttendInOneDay(89, dateBegin, dateEnd);
        System.out.println(i);
    }

}













