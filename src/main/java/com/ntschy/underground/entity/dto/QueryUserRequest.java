package com.ntschy.underground.entity.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class QueryUserRequest {
    private String name;

    private Integer status;

    private String startTime;

    private String endTime;

    @NotNull(message = "currPage不能为空")
    private Integer currPage;

    @NotNull(message = "pageSize不能为空")
    private Integer pageSize;
}
