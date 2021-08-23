package com.ntschy.underground.entity.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class ModifyUserRequest {

    private String userId;

    private String account;

    private String password;

    private String name;

    private String department;

    private String phone;

    private String sex;

    private String roleId;

    private String createTime;

    @NotNull(message = "操作类型不能为空")
    private Integer operateType;
}
