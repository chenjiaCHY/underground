/*
 * Copyright (c) 2021. All Rights Reserved.
 * ProjectName: underground
 * FileName: AddProjectFileRequest.java
 * Author: 陈佳
 * Date: 2021/8/25 下午3:50
 * Version: 1.0
 * LastModified
 *
 */

package com.ntschy.underground.entity.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@Data
public class AddProjectFileRequest {

    @NotBlank(message = "projectId不能为空")
    private String projectId;

    @NotEmpty(message = "fileNames不能为空")
    private List<String> fileNames;
}
