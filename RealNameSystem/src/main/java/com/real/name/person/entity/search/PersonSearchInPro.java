package com.real.name.person.entity.search;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.real.name.person.entity.Person;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PersonSearchInPro extends Person {

    @JsonIgnore
    private String projectCode;

    /**
     * 是否有合同
     * 1是 0否
     */
    @JsonIgnore
    private Integer hasContract;


    @JsonIgnore
    private Integer subContractorId;

    @JsonIgnore
    private Integer pageNum = 0;

    @JsonIgnore
    private Integer pageSize = 10;
}
