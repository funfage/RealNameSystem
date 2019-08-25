package com.real.name.nation.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Desc TODO
 * @Author hp
 * @Date 2019/5/9 16:23
 **/
@Data
@NoArgsConstructor
public class NationalPerson {

    private String projectCode;
    private String corpCode;
    private String corpName;
    private Integer teamSysNo;
    private String teamName;
    private List<Worker> workerList;


    @Data
    @NoArgsConstructor
    public class Worker {
        private String workerName;
        private Integer isTeamLeader;
        private String idCardType;
        private String workType;
        private Integer workRole;
        private String idCardNumber;
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
        private String Specialty;
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
    }


}
