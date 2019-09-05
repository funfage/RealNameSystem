package com.real.name.subcontractor.entity.search;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.real.name.subcontractor.entity.SubContractor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SubContractorSearchInPro extends SubContractor {

    /**
     * 项目编码
     */
    @JsonIgnore
    private String projectCode;

    /**
     * 页数
     */
    @JsonIgnore
    private Integer pageNum = 0;

    /**
     * 每页条数
     */
    @JsonIgnore
    private Integer pageSize = 20;

}
