package com.real.name.person.entity;

import lombok.Data;

@Data
public class PersonQuery extends Person {

    private String nameOrIdCard;

    private Integer workRole;

    private Integer ageBegin;

    private Integer ageEnd;

    private Integer pageNum = 0;

    private Integer pageSize = 10;

    public PersonQuery() {

    }

}
