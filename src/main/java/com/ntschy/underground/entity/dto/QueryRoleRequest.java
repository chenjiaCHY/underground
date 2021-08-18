package com.ntschy.underground.entity.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class QueryRoleRequest {
    // 角色名称
    private String roleName;

    // 角色状态
    private Integer roleStatus;

    // 当前页码
    @NotNull(message = "currPage不能为空")
    private Integer currPage;

    // 每页条数
    @NotNull(message = "pageSize不能为空")
    private Integer pageSize;
}
