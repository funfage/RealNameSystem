package com.real.name.pay.entity;

import com.real.name.project.entity.ProjectDetailQuery;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PayInfo {

    private Long payId;

    private ProjectDetailQuery projectDetailQuery;

    /**
     * 工资打发银行卡号
     */
    private String payBankCardNumber;

    /**
     * 工资代发银行代码
     */
    private String payBankCode;

    /**
     * 工资代发银行名称
     */
    private String payBankName;

    /**
     * 应发金额
     */
    private Float totalPayAmount;

    /**
     * 实发金额
     */
    private Float actualAmount;

    /**
     * 是否为补发
     */
    private Integer isBackPay;

    /**
     * 发放日期
     */
    private Date balanceDate;

    /**
     * 补发月份
     */
    private Date backPayMonth;

    /**
     * 第三方工资单编号
     */
    private String thirdPayRollCode;

    /**
     * 上传文件的文件名
     */
    private String suffixName;

    /**
     * 是否成功上传到全国平台
     */
    private Integer uploadStatus;

}
















