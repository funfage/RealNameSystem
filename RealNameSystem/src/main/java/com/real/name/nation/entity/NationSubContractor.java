package com.real.name.nation.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class NationSubContractor {

    private String projectCode;

    private String corpCode;

    private String corpName;

    private String corpType;

    private String entryTime;

    private String exitTime;

    private List<BankInfo> bankInfos;

    private String pmName;

    private String pmIDCardType;

    private String pmIDCardNumber;

    private String pmPhone;

    @Data
    public class BankInfo {

        private String bankCode;

        private String bankName;

        private String bankNumber;

        private String bankLinkNumber;

        public BankInfo() {
        }
    }



}
