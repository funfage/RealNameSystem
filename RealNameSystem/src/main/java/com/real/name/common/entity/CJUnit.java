package com.real.name.common.entity;

import lombok.Data;

import java.util.List;

/**
 * @Desc TODO
 * @Author hp
 * @Date 2019/5/6 12:10
 **/
@Data
public class CJUnit  {
    private String projectCode;
    private String corpCode;
    private String corpName;
    private String corpType;
    private String entryTime;
    private String exitTime;
    private List bankInfos;
    private String pmName;
    private String pmIDCardType;
    private String pmIdCardNumber;
    private String pmPhone;
}
