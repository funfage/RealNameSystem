package com.real.name.person.entity.search;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.real.name.person.entity.Person;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PersonSearch extends Person {

    @JsonIgnore
    private String nameOrIdCard;

    @JsonIgnore
    private Integer workRole;

    @JsonIgnore
    private Integer ageBegin;

    @JsonIgnore
    private Integer ageEnd;

    @JsonIgnore
    private Integer pageNum = 0;

    @JsonIgnore
    private Integer pageSize = 10;

}
