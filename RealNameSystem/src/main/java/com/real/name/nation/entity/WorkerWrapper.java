package com.real.name.nation.entity;

import lombok.Data;

@Data
public class WorkerWrapper extends Worker {
    private String projectCode;
    private String corpCode;
    private String corpName;
    private String teamName;
    private Integer teamSysNo;
}
