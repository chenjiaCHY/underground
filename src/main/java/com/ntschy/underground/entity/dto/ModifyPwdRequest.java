/*
 * Copyright (c) 2021. All Rights Reserved.
 * ProjectName: underground
 * FileName: ModifyPwdRequest.java
 * Author: 陈佳
 * Date: 2021/8/23 上午9:51
 * Version: 1.0
 * LastModified
 *
 */

package com.ntschy.underground.entity.dto;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

@Data
public class ModifyPwdRequest {

    @NotBlank(message = "登录名不能为空")
    private String account;

    @NotBlank(message = "新密码不能为空")
    @Length(min = 8, max = 16, message = "密码长度必须大于8位且不大于16位")
    private String newPwd;
}
