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
    DEVICE_PASS_ERROR(21, "设备密码错误"),
    PERSON_HAS_IN_PROJECT(22, "该人员以及添加到该项目"),
    PERSON_HAS_IN_WORKER(23, "该人员以及添加到该班组"),
    FACE_EMPTY(24, "查询不到人脸设备"),
    PROJECT_NO_BIND_FACE(25, "该项目未绑定人脸设备"),
    GENERATE_IMAGE_ERROR(26, "生成头像失败"),
    ID_CARD_NUMBER_REPEAT(27, "身份证卡号重复"),
    NO_HEARTBEAT(28, "未接收到设备的心跳信息"),
    RESET_DEVICE_ERROR(29, "重置报文失败"),
    EMPTY_NAME(30, "文件名称为空"),
    DEVICE_EMPTY(31, "设备不存在"),
    DEVICE_TYPE_ERROR(32, "设备类型不正确"),
    IP_PORT_REPEAT(33, "设备的ip和端口号重发"),
    ADMIN_GROUP_ERROR(34, "管理员班组只能有一个"),
    DEVICE_KEY_NOT_MATCH(35, "设备序列号不匹配"),
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
