package com.real.name.nation.entity;

import lombok.Data;

import java.util.List;
@Data
public class NationalGroup {

    private String projectCode;
    private String corpCode;
    private String corpName;
    private Integer teamSysNo;
    private String teamName;
    private String responsiblePersonName;
    private String responsiblePersonPhone;
    private String responsiblePersonIDCardType;
    private String responsiblePersonIDNumber;
    private String remark;
    private String entryTime;
    private String exitTime;
    private List<NationalAttachment> entryAttachments;
    private List<NationalAttachment> exitAttachments;
}
