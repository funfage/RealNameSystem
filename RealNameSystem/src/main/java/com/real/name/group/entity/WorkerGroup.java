package com.real.name.group.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

/**
 * 班组
 */
@Data
@Entity
public class WorkerGroup {

    /**
     * 班组编号
     */
    @Id
    private Integer teamSysNo;

    /**
     * 项目编码
     */
    private String projectCode;

    /**
     * 班组所在企业统一社会信用代码，如果无统一社会信用代码，则用组织机构代码
     */
    private String corpCode;

    /**
     * 班组所在企业名称
     */
    private String corpName;

    /**
     * 班组名称，同一个项目和参建单位下面不能重复
     */
    private String teamName;

    /**
     * 责任人姓名，班组所在企业负责人
     */
    private String responsiblePersonName;

    /**
     * 责任人联系电话
     */
    private String responsiblePersonPhone;

    /**
     * 责任人证件类型。参考人员证件类型字典表
     */
    private Integer responsiblePersonIdCardType;

    /**
     * 责任人证件号码。AES
     */
    private String responsiblePersonIdNumber;

    /**
     * 备注
     */
    private String remark;

    private Date entryTime;
    private Date exitTime;
    private String entryAttachments;
    private String exitAttachments;
}