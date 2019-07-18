package com.real.name.person.entity;

import com.real.name.group.entity.WorkerGroup;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PersonQuery extends Person {

    private WorkerGroup workerGroup;

    public PersonQuery() {

    }

}
