package com.real.name.pay.query;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.real.name.pay.entity.PayInfo;
import lombok.Data;

@Data
public class PayInfoQuery extends PayInfo {

    private String nameOrIdCardNumber;

    private String projectCode;

    @JsonIgnore
    private Integer pageNum = 10;

    @JsonIgnore
    private Integer pageSize = 20;

}
