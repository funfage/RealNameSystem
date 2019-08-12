package com.real.name.nation.query;

import com.real.name.person.entity.Person;
import lombok.Data;

import java.util.List;

@Data
public class ProjectWorkerQuery {

    private String projectCode;

    private String corpCode;

    private String corpName;

    private String teamSysNo;

    private String teamName;

    private List<Person> workerList;

}
