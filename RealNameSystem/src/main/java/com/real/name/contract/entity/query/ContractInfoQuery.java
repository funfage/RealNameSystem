package com.real.name.contract.entity.query;

import com.real.name.contract.entity.ContractInfo;
import com.real.name.subcontractor.entity.SubContractor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ContractInfoQuery extends ContractInfo {

    private SubContractor subContractor;

}
