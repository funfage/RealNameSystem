package com.real.name.record.entity;

import com.real.name.person.entity.Person;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
public class PersonAttend {

    private Long personAttendId;

    private Person person;

    private Date entryExitTime;

    private Integer direction;

    private Integer uploadStatus;

    public PersonAttend(Person person, Date entryExitTime, Integer direction) {
        this.person = person;
        this.entryExitTime = entryExitTime;
        this.direction = direction;
    }
}
