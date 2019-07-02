package com.real.name.common.result;

import lombok.Getter;

@Getter
public enum ResultError {
    OPERATOR_ERROR(-1, "operator error"),
    CONTENT_EMPTY(1, "内容不能为空"),
    CONTENT_ERROR(2, "内容不正确"),
    PERSON_EXIST(3, "人员已存在"),
    PERSON_NOT_EXIST(4, "人员不存在"),
    PROJECT_EXIST(5, "项目已存在"),
    PROJECT_NOT_EXIST(6, "不存在该项目"),
    ID_CARD_ERROR(6, "身份证号码不正确"),
    PERSON_NAME_ERROR(7, "人员姓名不正确"),
    DEVICE_EXIST(8, "设备已存在"),
    DEVICE_NOT_EXIST(9, "读头设备不存在"),
    GROUP_EXIST(10, "班组已存在"),
    GROUP_NOT_EXIST(11, "班组不存在"),
    PHONE_ERROR(12, "手机号码不正确"),
    PARAM_ERROR(13, "参数有误"),
    USERNAME_OR_PASSWORD_ERROR(14, "用户名或密码错误"),
    PERSONID_EMPTY(15, "人员ID为空"),
    PERSON_EMPTY(16, "该人员不存在"),
    ID_CARD_REPEAT(17, "身份证号重复"),
    NATIONAL_ERROR(18, "全国平台相关错误"),
    RESPONSE_ERROR(19, "给设备发送POST请求返回信息为空"),
    FACE_ISSUE_ERROR(20, "人脸设备下发失败"),
    LOGIN_TOKEN_ERROR(98, "登录信息中没有Token"),
    NETWORK_ERROR(99, "网络超时,请重试"),
    DELETE_ERROR(100, "删除失败"),
    INSERT_ERROR(101, "添加失败"),
    UPDATE_ERROR(102, "修改失败"),
    QUERY_ERROR(103, "查询失败"),
    QUERY_EMPTY(104, "查询结果为空"),
    ;

    private Integer code;

    private String message;

    ResultError(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
