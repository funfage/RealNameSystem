package com.real.name.common.entity;

import lombok.Data;

/**
 * @Desc TODO
 * @Author hp
 * @Date 2019/5/6 14:06
 **/
@Data
public class BankInfo {
    private String bankCode;
    private String bankName;
    private String bankNumber;
    private String bankLinkNumber;

    public BankInfo(String bankCode,String bankName,String bankNumber,String bankLinkNumber) {
        this.bankCode = bankCode;
        this.bankLinkNumber = bankLinkNumber;
        this.bankName = bankName;
        this.bankNumber = bankNumber;
    }
}
