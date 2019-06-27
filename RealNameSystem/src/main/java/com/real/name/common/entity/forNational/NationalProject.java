package com.real.name.common.entity.forNational;

import lombok.Data;

import java.util.List;

/**
 * @Desc TODO
 * @Author fxy
 * @Date 2019/5/9 10:36
 **/
@Data
public class NationalProject {
    private String projectCode;
    private String description;
    private String buildCorpName;
    private String buildCorpCode;
    private String corpCode;
    private String corpName;
    private String buildPlanNum;
    private String prjPlanNum;
    private Double invest;
    private Double buildingArea;
    private Double buildingLength;
    private String linkman;
    private String linkPhone;
    private String prjStatus;
    private Double lat;
    private Double lng;
    private String address;
    private String approvalNum;
    private String approvalLevelNum;
    private String prjSize;
    private String propertyNum;
    private String functionNum;
    private String nationNum;
    private List<Worker> contractList;
    private List<Worker> dataList;
    private Integer teamSysNo;
    private String payMonth;
    private List<NationalAttachment> attachments;
    private List<Worker> detailList;
}
