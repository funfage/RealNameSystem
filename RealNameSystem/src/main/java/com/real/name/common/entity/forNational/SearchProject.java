package com.real.name.common.entity.forNational;

import lombok.Data;

/**
 * @Desc TODO
 * @Author hp
 * @Date 2019/5/9 11:07
 **/
@Data
public class SearchProject {

    private Integer pageIndex;
    private Integer pageSize;
    private Integer teamSysNo;


    private String projectCode;


    /**
     * 以下两个参数不能同时为空
     */
    private String contractorCorpCode;
    private String contractorCorpName;

    /**
     * 某些情况下需要用到的字段
     */
    private String corpCode;
    private String corpName;
    private String idCardType;
    private String idCardNumber;
    private String date;
    private String trainingDate;
    private String typeCode;
    private String creditType;
    public SearchProject(Integer pageIndex,Integer pageSize) {
        this.pageIndex = pageIndex;
        this.pageSize = pageSize;
    }
}
