package com.ntschy.underground.entity.base;

import com.ntschy.underground.utils.JsonUtils;
import lombok.Data;

import java.io.Serializable;

@Data
public class Result<T> implements Serializable {

    private Boolean success;
    private String errorCode;
    private String message;
    private T data;

    public Result() {
        this.success = true;
    }

    public Result(Boolean success) {
        this.success = success;
    }

    public Result(T data) {
        this.success = true;
        this.data = data;
    }

    public Result(Boolean success, T data) {
        this.success = success;
        this.data = data;
    }

    public Result(Boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public Result(Boolean success, String errorCode, String message) {
        this.success = success;
        this.errorCode = errorCode;
        this.message = message;
    }

    @Override
    public String toString() {
        return JsonUtils.toJsonStr(this);
    }
}
