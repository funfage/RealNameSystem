package com.real.name.contract.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ContractFile {

    private Integer id;

    private Integer contractId;

    private String fileName;

    public ContractFile(Integer contractId, String fileName) {
        this.contractId = contractId;
        this.fileName = fileName;
    }
}
