package com.real.name.contract.query;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.real.name.contract.entity.ContractInfo;
import lombok.Data;

@Data
public class ContractInfoQuery extends ContractInfo {

    private String projectName;

    private String personName;

    private String subordinateCompany;

    private String corpCode;

    @JsonIgnore
    private Integer pageNum = 0;

    @JsonIgnore
    private Integer pageSize = 20;

}
