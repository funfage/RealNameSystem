package com.real.name.project.entity;

import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Data
@Entity
@DynamicInsert
@DynamicUpdate
public class Project {

    @Id
    private String projectCode;

    /**
     * 总承包单位统一社会信用代码
     */
    private String contractorCorpCode;

    /**
     * 总承包单位名称
     */
    private String contractorCorpName;

    /**
     * 项目名称
     */
    private String name;

    /**
     * 项目简介
     */
    private String description;

    /**
     * 项目分类
     */
    private String category;

    /**
     * 建设单位名称
     */
    private String buildCorpName;

    /**
     * 建设单位统一社会信用代码，如果无统一社会信用代码，则用组织机构代码
     */
    private String buildCorpCode;

    /**
     * 施工许可证
     */
    private String builderLicenses;

    /**
     * 建设用地规划许可证编号
     */
    private String buildPlanNum;

    /**
     * 建设工程规划许可证编号
     */
    private String prjPlanNum;

    /**
     * 项目所在地
     */
    private String areaCode;

    /**
     * 总投资，单位：（万元）
     */
    private Double invest = 0.0;

    /**
     * 总面积，单位：平方米
     */
    private Double buildingArea = 0.0;

    /**
     * 总长度，单位：米
     */
    private Double buildingLength = 0.0;

    /**
     * 开工日期
     */
    private Date startDate = new Date();

    /**
     * 竣工日期
     */
    private Date completeDate = new Date();

    /**
     * 项目联系人名称
     */
    private String linkMan;

    /**
     * 项目联系人电话
     */
    private String linkPhone;

    /**
     * 项目状态，001:筹备，002:立项，003:在建，004:完工，005:停工
     */
    private Integer prjStatus = 1;

    /**
     * 经度
     */
    private Double lat = 0.0;
    /**
     * 维度
     */
    private Double lng = 0.0;

    /**
     * 项目位置
     */
    private String address;

    /**
     * 立项文号
     */
    private String approvalNum;

    /**
     * 立项级别
     */
    private Integer approvalLevelNum;

    /**
     * 建筑规模，01:大型,02:中型，03:小型
     */
    private String prjSize;

    /**
     * 001：新建，002：改建，003：扩建，004：恢复，005：迁建，006：拆除，099：其他
     */
    private String propertyNum;
    /**
     * 工程用途，参考文档
     */
    private String functionNum;
    /**
     * 按照《世界各国和地区名称代码》GB/T 2659-2000 规定的国家和地区名称及代码。
     */
    private Integer nationNum;

    @OneToMany(mappedBy = "project", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    private List<ProjectPersonDetail> projectPersonDetailList;

    public Project(String projectName, String projectCompany, String projectLocal, String projectType, Integer projectContact) {
        this.name = projectName;
        this.buildCorpName = projectCompany;
        this.address = projectLocal;
        this.linkMan = projectContact.toString();
        this.category = projectType;
    }

    public Project() { }

    @Override
    public String toString() {
        return "Project{" +
                "projectCode='" + projectCode + '\'' +
                ", contractorCorpCode='" + contractorCorpCode + '\'' +
                ", contractorCorpName='" + contractorCorpName + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", category='" + category + '\'' +
                ", buildCorpName='" + buildCorpName + '\'' +
                ", buildCorpCode='" + buildCorpCode + '\'' +
                ", builderLicenses='" + builderLicenses + '\'' +
                ", buildPlanNum='" + buildPlanNum + '\'' +
                ", prjPlanNum='" + prjPlanNum + '\'' +
                ", areaCode='" + areaCode + '\'' +
                ", invest=" + invest +
                ", buildingArea=" + buildingArea +
                ", buildingLength=" + buildingLength +
                ", startDate=" + startDate +
                ", completeDate=" + completeDate +
                ", linkMan='" + linkMan + '\'' +
                ", linkPhone='" + linkPhone + '\'' +
                ", prjStatus=" + prjStatus +
                ", lat=" + lat +
                ", lng=" + lng +
                ", address='" + address + '\'' +
                ", approvalNum='" + approvalNum + '\'' +
                ", approvalLevelNum=" + approvalLevelNum +
                ", prjSize='" + prjSize + '\'' +
                ", propertyNum='" + propertyNum + '\'' +
                ", functionNum='" + functionNum + '\'' +
                ", nationNum=" + nationNum +
                '}';
    }
}