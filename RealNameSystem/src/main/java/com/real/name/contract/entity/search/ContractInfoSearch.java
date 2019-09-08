package com.real.name.contract.entity.search;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.real.name.contract.entity.ContractInfo;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ContractInfoSearch extends ContractInfo {

    private String projectName;

    private String personName;

    private String subordinateCompany;

    private String corpCode;

    @JsonIgnore
    private Integer pageNum = 0;

    @JsonIgnore
    private Integer pageSize = 20;

}
