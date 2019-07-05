package com.real.name.common.exception;

import com.real.name.common.result.ResultVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;


@ControllerAdvice
public class AttendanceExceptionHandler {

    private Logger logger = LoggerFactory.getLogger(AttendanceExceptionHandler.class);

    @ExceptionHandler(value = AttendanceException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ResponseBody
    public ResultVo AttendanceExceptionHandler(AttendanceException e) {
        logger.error("错误信息", e);
        return ResultVo.failure(e.getCode(), e.getMessage());
    }

    @ExceptionHandler(NullPointerException.class)
    @ResponseBody
    public ResultVo NullPointerExceptionHandler(NullPointerException e) {
        logger.error("出现空指针异常", e);
        return ResultVo.failure(e.getMessage());
    }

    @ExceptionHandler(ArithmeticException.class)
    @ResponseBody
    public ResultVo ArithmeticExceptionHandler(ArithmeticException e) {
        logger.error("出现算术异常", e);
        return ResultVo.failure(e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResultVo exceptionHandler(Exception e) {
        logger.error("出现异常，e:{}", e);
        return ResultVo.failure(e.getMessage());
    }
}
