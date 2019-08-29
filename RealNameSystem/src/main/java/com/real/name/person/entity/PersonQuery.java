package com.real.name.person.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

@Data
public class PersonQuery extends Person {

    @JsonIgnore
    private String projectCode;

    @JsonIgnore
    private Integer subContractorId;

    /**
     * 是否有合同
     * 1是 0否
     */
    @JsonIgnore
    private Integer hasContract;

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

    public PersonQuery() {

    }

}
