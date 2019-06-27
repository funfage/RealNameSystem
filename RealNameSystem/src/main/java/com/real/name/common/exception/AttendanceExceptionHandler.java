package com.real.name.common.exception;

import com.real.name.common.result.ResultVo;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class AttendanceExceptionHandler {

    @ExceptionHandler(value = AttendanceException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ResponseBody
    public ResultVo handleResponseBankException(AttendanceException e) {
        return ResultVo.failure(e.getCode(), e.getMessage());
    }
}
