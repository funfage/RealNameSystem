package com.real.name.common.entity.forNational;

import lombok.Data;

import java.util.List;

/**
 * @Desc TODO
 * @Author hp
 * @Date 2019/5/9 16:33
 **/
@Data
public class NationalWorkerInfo {
    private String projectCode;
    private String corpCode;
    private String corpName;
    private Integer teamSysNo;
    private String teamName;
    private List<Worker> workerList;

    public NationalWorkerInfo() {

    }

    public NationalWorkerInfo(String projectCode, String corpCode, String corpName, Integer teamSysNo, String teamName) {
        this.projectCode = projectCode;
        this.corpCode = corpCode;
        this.corpName = corpName;
        this.teamName = teamName;
        this.teamSysNo = teamSysNo;
    }
}
