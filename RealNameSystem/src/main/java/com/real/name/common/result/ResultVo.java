package com.real.name.common.result;

import lombok.Getter;

@Getter
public class ResultVo {

    private Integer code;

    private Boolean success;

    private Object data;

    private String message;

    public static ResultVo success(Object data, String msg) {
        return new ResultVo(0, true, data, msg);
    }

    public static ResultVo success(Object data) {
        return new ResultVo(0, true, data, "operate success");
    }

    public static ResultVo success(String msg) {
        return new ResultVo(0, true, null, msg);
    }

    public static ResultVo success() {
        return new ResultVo(0, true, null, "operate success");
    }

    public static ResultVo failure(ResultError error) {
        return new ResultVo(error.getCode(), false, error.getMessage());
    }

    public static ResultVo failure(Integer code, String message) {
        return new ResultVo(code, false, message);
    }

    public ResultVo(Integer code, Boolean success, Object data, String msg) {
        this.code = code;
        this.success = success;
        this.data = data;
        this.message = msg;
    }

    public ResultVo(Integer code, Boolean success, Object data) {
        this.code = code;
        this.success = success;
        this.data = data;
    }

    public ResultVo(Integer code, Boolean success, String msg) {
        this.code = code;
        this.success = success;
        this.message = msg;
    }

    public ResultVo(Integer code, Boolean success) {
        this.code = code;
        this.success = success;
    }


    @Override
    public String toString() {
        return "ResultVo{" +
                "code=" + code +
                ", success=" + success +
                ", data=" + data +
                ", msg='" + message + '\'' +
                '}';
    }
}
