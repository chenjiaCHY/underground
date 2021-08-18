package com.ntschy.underground.handler;

import java.io.Serializable;

public enum ExceptionEnum implements Serializable {

    DEFAULT_EXCEPTION(ExpCodePrefix.COMMON_EXP_PREFIX + "000", "未知异常，请查看日志"),
    DEFAULT_EXCEPTION_WITH_CUSTOMISE_MESSAGE(ExpCodePrefix.COMMON_EXP_PREFIX + "001"),
    PARAMS_TYPE_CAST_EXCEPTION(ExpCodePrefix.ACCOUNT_EXP_PREFIX + "005", "参数类型转换错误"),
    REQUEST_PARAMS_EXCEPTION(ExpCodePrefix.ACCOUNT_EXP_PREFIX + "005", "request param is null or invalid"),
    TOKEN_EXCEPTION("403", "token is null or invalid"),
    NO_PERMISSIONS(ExpCodePrefix.ACCOUNT_EXP_PREFIX + "000", "没有权限"),
    PWD_EASY_EXCEPTION(ExpCodePrefix.ACCOUNT_EXP_PREFIX + "005", "密码需由字母和数字组合，请重新设置"),
    ORIGION_PWD_EXCEPTION(ExpCodePrefix.ACCOUNT_EXP_PREFIX + "006", "原密码不正确"),
    SAME_PWD_EXCEPTION(ExpCodePrefix.ACCOUNT_EXP_PREFIX + "007", "新密码不能和原密码相同");


    private String code;
    private String message;

    ExceptionEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }

    ExceptionEnum(String code) {
        this.code = code;
    }

    public String getCode() {
        return this.code;
    }

    public String getMessage() {
        return this.message;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
