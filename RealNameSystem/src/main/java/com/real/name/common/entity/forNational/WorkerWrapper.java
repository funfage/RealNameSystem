package com.real.name.common.entity.forNational;

import lombok.Data;

/**
 * @Desc TODO
 * @Author hp
 * @Date 2019/5/9 20:26
 **/
@Data
public class WorkerWrapper extends Worker {
    private String projectCode;
    private String corpCode;
    private String corpName;
    private String teamName;
    private Integer teamSysNo;
}
