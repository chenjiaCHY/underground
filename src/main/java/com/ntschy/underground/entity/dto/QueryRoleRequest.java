package com.ntschy.underground.entity.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class QueryRoleRequest {

    // 当前页码
    @NotNull(message = "currPage不能为空")
    private Integer currPage;

    // 每页条数
    @NotNull(message = "pageSize不能为空")
    private Integer pageSize;
}
