package com.real.name.common.entity;

import lombok.Data;

/**
 * @Desc TODO
 * @Author hp
 * @Date 2019/5/6 13:33
 **/
@Data
public class Base {
    private String appid = "44010620190510001";
    private String format = "json";
    private String method;
    private String nonce;
    private String version = "1.0";
    private String timestamp;
    private String corpsign;
    private String sign;
}
