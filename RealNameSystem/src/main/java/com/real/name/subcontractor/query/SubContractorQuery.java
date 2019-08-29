package com.real.name.subcontractor.query;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.real.name.subcontractor.entity.SubContractor;
import lombok.Data;

@Data
public class SubContractorQuery extends SubContractor {

    /**
     * 合同签订率
     */
    private Float contractSignRate;

    /**
     * 在场人数
     */
    private Integer attendNum;

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
