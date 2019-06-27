package com.real.name.common.entity.forNational;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @Desc TODO
 * @Author hp
 * @Date 2019/5/9 16:23
 **/
@Data
public class Worker {

    private String projectCode;
    private String payrollCode;
    private Integer teamSysNo;

    private String payBankName;
    private String payRollBankCode;
    private String  balanceDate;
    private String  payBankCode;
    private String  payBankCardNumber;
    private Integer isBackPay;
    private Double totalPayAmount;
    private Double actualAmount;
    private String workerName;
    private Integer isTeamLeader;
    private String idCardType;
    private String idCardNumber;
    private String workType;
    private Integer workRole;
    private String issueCardDate;
    private String issueCardPic;
    private String cardNumber;
    private String payRollBankCardNumber;
    private String payRollBankName;
    private String bankLinkNumber;
    private String payRollTopBankCode;
    private Integer hasBuyInsurance;
    private String nation;
    private String address;
    private String headImage;
    private String politicsType;
    private String joinedTime;
    private String cellPhone;
    private String cultureLevelType;
    private String specialty;
    private Integer hasBadMedicalHistory;
    private String urgentLinkMan;
    private String urgentLinkManPhone;
    private String workDate;
    private String maritalStatus;
    private String grantOrg;
    private String positiveIDCardImage;
    private String negativeIDCardImage;
    private String startDate;
    private String expiryDate;
    private String endDate;

    /**
     * 部分地方需要使用
     */
    private String Date;
    private Integer type;
    private String voucher;
    private String corpCode;
    private String corpName;
    private Integer unit;
    private Double unitPrice;
    private Integer contractPeriodType;
    private List<NationalAttachment> attachments;
    private String direction;
    private String image;
    private String channel;
    private String attendType;
    private Double lng;
    private Double lat;
    private String payMonth;
}
