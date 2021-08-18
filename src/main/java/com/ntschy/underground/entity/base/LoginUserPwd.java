package com.ntschy.underground.entity.base;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

@Data
public class LoginUserPwd {
    @NotBlank(message = "登录名不能为空")
    private String account;

    @NotBlank(message = "密码不能为空")
    private String pwd;

    @NotBlank(message = "新密码不能为空")
    @Length(min = 8, max = 16, message = "密码长度必须大于8位且不大于16位")
    private String modifyPwd;
}
