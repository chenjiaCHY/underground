/*
 * Copyright (c) 2021. All Rights Reserved.
 * ProjectName: underground
 * FileName: ReviewInspectionRequest.java
 * Author: 陈佳
 * Date: 2021/8/26 下午3:48
 * Version: 1.0
 * LastModified
 *
 */

package com.ntschy.underground.entity.dto;

import com.ntschy.underground.enums.ProgressType;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class ReviewInspectionRequest {

    @NotBlank(message = "inspectionId不能为空")
    private String inspectionId;

    private String rectifyComment;

    @NotNull(message = "progress不能为空")
    private ProgressType progress;
}
