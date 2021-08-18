package com.ntschy.underground.handler;

import lombok.Data;

import java.io.Serializable;

@Data
public class CommonException extends RuntimeException implements Serializable {
    private ExceptionEnum exception;

    public CommonException() {
        super();
        this.exception = ExceptionEnum.DEFAULT_EXCEPTION;
    }

    public CommonException(String message) {
        super(message);
        this.exception = ExceptionEnum.DEFAULT_EXCEPTION_WITH_CUSTOMISE_MESSAGE;
        this.exception.setMessage(message);
    }

    public CommonException(ExceptionEnum exception) {
        super(exception.getMessage());
        this.exception = exception;
    }

    public ExceptionEnum getCodeEnum() {
        return this.exception;
    }
}
