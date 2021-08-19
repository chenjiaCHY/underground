/*
 * Copyright (c) 2021. All Rights Reserved.
 * ProjectName: underground
 * FileName: UserLogin.java
 * Author: 陈佳
 * Date: 2021/8/19 下午3:49
 * Version: 1.0
 * LastModified
 *
 */

package com.ntschy.underground.entity.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class UserLogin {

    @NotBlank(message = "登录名不能为空")
    private String account;

    @NotBlank(message = "密码不能为空")
    private String pwd;
}
