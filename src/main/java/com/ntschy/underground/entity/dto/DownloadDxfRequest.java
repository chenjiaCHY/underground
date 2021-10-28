/*
 * Copyright (c) 2021. All Rights Reserved.
 * ProjectName: underground
 * FileName: DownloadDxfRequest.java
 * Author: 陈佳
 * Date: 2021/10/11 下午1:49
 * Version: 1.0
 * LastModified
 */

package com.ntschy.underground.entity.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class DownloadDxfRequest {
    @NotBlank(message = "layerNames不能为空")
    private String layerNames;

    @NotBlank(message = "points不能为空")
    private String points;
}
