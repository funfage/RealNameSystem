package com.real.name.nation.entity;

import lombok.Data;

import java.util.List;

@Data
public class NationalContractInfo {

    private String projectCode;

    private List<Contract> contractList;

    @Data
    public class Contract {
        private String corpCode;

        private String corpName;

        private String idCardType;

        private String idCardNumber;

        private Integer contractPeriodType;

        private String startDate;

        private String endDate;
    }



}
