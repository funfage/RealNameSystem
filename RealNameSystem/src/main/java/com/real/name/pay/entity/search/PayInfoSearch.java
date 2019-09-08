package com.real.name.pay.entity.search;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.real.name.pay.entity.PayInfo;
import lombok.Data;

@Data
public class PayInfoSearch extends PayInfo {
    private String nameOrIdCardNumber;

    private String projectCode;

    @JsonIgnore
    private Integer pageNum = 10;

    @JsonIgnore
    private Integer pageSize = 20;
}
