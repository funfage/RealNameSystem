package com.real.name.common.entity.forNational;

import lombok.Data;

import java.util.List;

/**
 * @Desc TODO
 * @Author hp
 * @Date 2019/5/9 11:57
 **/
@Data
public class NationalGroup {

    private Integer teamSysNo;
    private String requestSerialCode;

    private String projectCode;
    private String corpCode;
    private String corpName;
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
