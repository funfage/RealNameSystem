package com.real.name.contract.entity;

import com.real.name.project.entity.ProjectDetailQuery;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
public class ContractInfo {

    private Integer contractId;

    private ProjectDetailQuery projectDetailQuery;

    /**
     * 合同期限类型
     * 0,固定期限合同
     * 1,以完成一定工作为期限的合同
     */
    private Integer contractPeriodType;

    /**
     * 生效日期，yyyy-MM-dd
     */
    private Date startDate;

    /**
     * 失效日期，yyyy-MM-dd
     */
    private Date endDate;

    /**
     * 是否上传到全国平台
     * 1是 0否
     */
    private Integer uploadStatus;

    /**
     * 合同部的文件集合
     */
    private List<ContractFile> contractFileList;

}
