package com.real.name.common.entity;

import lombok.Data;

/**
 * @Desc TODO
 * @Author hp
 * @Date 2019/5/6 15:45
 **/
@Data
public class ProjectInfo {
    private int pageIndex;
    private int pageSize;
    private String projectCode;
    private String contractorCorpCode;
    private String contractorCorpName;
}
