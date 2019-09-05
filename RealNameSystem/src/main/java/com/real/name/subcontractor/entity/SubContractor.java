package com.real.name.subcontractor.entity;

import com.real.name.project.entity.Project;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;

@Data
@NoArgsConstructor
@ToString
public class SubContractor {

    private Integer subContractorId;

    private Project project;

    private String corpCode;

    private String corpName;

    private String corpType;

    private Date entryTime;

    private Date exitTime;

    private String bankCode;

    private String bankName;

    private String bankNumber;

    private String bankLinkNumber;

    private String pmName;

    private String pmIdCardNumber;

    private String pmPhone;

    private Date createTime;

    /**
     * 是否移出项目
     * 0移出
     * 1未移出
     */
    private Integer contractorStatus;

    private Integer uploadStatus;

    /**
     * 合同签订率
     */
    private Float contractSignRate;

    /**
     * 在场人数
     */
    private Integer attendNum;



}




