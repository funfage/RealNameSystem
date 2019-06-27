package com.real.name.common.exception;

import com.real.name.common.result.ResultError;
import lombok.Getter;

@Getter
public class AttendanceException extends RuntimeException {

    private Integer code;

    private String message;

    public AttendanceException(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public AttendanceException(ResultError error) {
        this(error.getCode(), error.getMessage());
    }

    public static AttendanceException emptyMessage(String message) {
        return new AttendanceException(1, message + ResultError.CONTENT_EMPTY.getMessage());
    }

    public static AttendanceException errorMessage(String message) {
        return new AttendanceException(2, message + ResultError.CONTENT_ERROR.getMessage());
    }
}
