package com.ntschy.underground.entity.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class ModifyUserRequest {

    private String userID;

    private String account;

    private String name;

    private Integer sex;

    private String phone;

    private String remark;

    private String roleID;

    private Integer status;

    @NotNull(message = "操作类型不能为空")
    private Integer operateLabel;
}
