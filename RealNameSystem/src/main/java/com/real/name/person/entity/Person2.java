package com.real.name.person.entity;

import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@DynamicUpdate
@DynamicInsert
@Table(name = "person")
public class Person2 {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer personId;

    /**
     * 姓名
     */
    private String personName;

    /**
     * 身份证
     */
    private String idCardNumber;

    /**
     * 是否是班组长;1:是，0：不是
     */
    private Integer isTeamLeader;

    /**
     * 证件类型
     */
    private Integer idCardType;

    /**
     * 当前工种
     */
    private String workType;

    /**
     * 工人类型;10:管理人员，20：建筑工人
     */
    private Integer workRole;

    /**
     * 发卡时间
     */
    private Date issueCardDate;

    /**
     * 办卡采集相片。不超过 50KB 的 Base64 字符串
     */
    private String issueCardPic;

    /**
     * 考勤卡号
     */
    private String cardNumber;

    /**
     * 发放工资银行卡号。AES
     */
    private String payRollBankCardNumber;

    /**
     * 发放工资银行名称
     */
    private String payRollBankName;

    /**
     * 发放工资卡银行联号
     */
    private String bankLinkNumber;

    /**
     * 发放工资卡银行
     */
    private Integer payRollTopBankCode;

    /**
     * 是否购买工伤或意外伤害保险 。1：是， 0：不是
     */
    private Integer hasBuyInsurance;

    /**
     * 民族。身份证上民族信息，如：汉，回，藏等
     */
    private String nation;

    /**
     * 政治面貌
     */
    private Integer politicsType;

    /**
     * 加入工会时间。如果加入工会，此字段必填；格式yyyy-MM-dd
     */
    private Date joinedTime;

    /**
     * 手机号码
     */
    private String cellPhone;

    /**
     * 文化程度，01小学, 02初中, 03高中, 04中专, 05大专, 06本科, 07硕士, 08博士, 99其他
     */
    private Integer cultureLevelType;

    /**
     * 特长
     */
    private String specialty;

    /**
     * 年龄
     */
    private Integer age;

    /**
     * 是否有重大病史，(暂时)1：是，0：不是
     */
    private Integer hasBadMedicalHistory;

    /**
     * 紧急联系人名字
     */
    private String urgentLinkMan;

    /**
     * 紧急联系人方式
     */
    private String urgentLinkManPhone;

    /**
     * 开始工作日期。格式：yyyy-MM-dd
     */
    private Date workDate;

    /**
     * 婚姻状况，01：未婚，02：已婚，03：离异，04：丧偶
     */
    private Integer maritalStatus;

    /**
     * 发证机关
     */
    private String grantOrg;

    /**
     * 证件有效期开始日期。格式 yyyy-MM-dd
     */
    private Date startDate;

    /**
     * 证件有效期结束日期。格式 yyyy-MM-dd
     */
    private Date expiryDate;

    /**
     * 0:女，1:男
     */
    private Integer gender;

    /**
     * 户籍
     */
    private String hometown;

    /**
     * 身份证住址
     */
    private String address;

    /**
     * 身份证索引号
     */
    private String idCardIndex;
    /**
     * 班组
     */
    private Integer groupNo;
}