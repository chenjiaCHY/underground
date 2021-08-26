/*
 * Copyright (c) 2021. All Rights Reserved.
 * ProjectName: underground
 * FileName: QueryInspectionRequest.java
 * Author: 陈佳
 * Date: 2021/8/25 下午5:00
 * Version: 1.0
 * LastModified
 *
 */

package com.ntschy.underground.entity.dto;

import com.ntschy.underground.enums.InspectionType;
import com.ntschy.underground.enums.ProgressType;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class QueryInspectionRequest {

    private ProgressType progress;

    private String createTime;

    private InspectionType type;

    @NotNull(message = "currPage不能为空")
    private Integer currPage;

    @NotNull(message = "pageSize不能为空")
    private Integer pageSize;
}
