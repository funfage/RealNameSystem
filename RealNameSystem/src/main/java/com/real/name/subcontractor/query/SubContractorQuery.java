package com.real.name.subcontractor.query;

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

}
