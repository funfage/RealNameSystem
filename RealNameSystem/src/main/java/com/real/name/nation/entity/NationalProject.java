package com.real.name.nation.entity;

import lombok.Data;

import java.util.List;

@Data
public class NationalProject {
    private String projectCode;
    private String contractorCorpCode;
    private String contractorCorpName;
    private String name;
    private String description;
    private String category;
    private String buildCorpName;
    private String buildCorpCode;
    //施工许可证。JSON 数组
    private String builderLicenses;
    private String buildPlanNum;
    private String prjPlanNum;
    private String areaCode;
    private Double invest;
    private Double buildingArea;
    private Double buildingLength;
    //开工日期，精确到天，格式：yyyy-MM-dd
    private String startDate;
    //竣工日期，精确到天，格式：yyyy-MM-dd
    private String completeDate;
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

}
