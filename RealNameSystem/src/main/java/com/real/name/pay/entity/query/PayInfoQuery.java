package com.real.name.pay.entity.query;

import com.real.name.pay.entity.PayInfo;
import com.real.name.subcontractor.entity.SubContractor;
import lombok.Data;

@Data
public class PayInfoQuery extends PayInfo {

    private SubContractor subContractor;

}
