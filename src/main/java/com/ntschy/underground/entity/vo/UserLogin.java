package com.ntschy.underground.entity.vo;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class UserLogin {

    @NotBlank(message = "登录名不能为空")
    private String account;

    @NotBlank(message = "密码不能为空")
    private String pwd;

    @NotNull(message = "登录类型不能为空")
    private Integer type;
}
