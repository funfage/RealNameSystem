package com.real.name.nation.entity;

import lombok.Data;

import java.util.List;

@Data
public class NationalPayInfo {

    private String projectCode;

    private String corpCode;

    private String corpName;

    private Integer teamSysNo;

    private String payMonth;

    private List<Detail> detailList;

    @Data
    public class Detail {
        private String idCardType;

        private String idCardNumber;

        private Integer days;

        private Double workHours;

        private String payRollBankCardNumber;

        private String payRollBankCode;

        private String payRollBankName;

        private String payBankCardNumber;

        private String payBankCode;

        private String payBankName;

        private Double totalPayAmount;

        private Double actualAmount;

        private Integer isBackPay;

        private String balanceDate;

        private String backPayMonth;

        private String thirdPayRollCode;

    }

}
