/*
 * Copyright (c) 2021. All Rights Reserved.
 * ProjectName: underground
 * FileName: AddProjectRequest.java
 * Author: 陈佳
 * Date: 2021/8/25 上午9:55
 * Version: 1.0
 * LastModified
 *
 */

package com.ntschy.underground.entity.dto;

import com.ntschy.underground.entity.ShapePoint;
import com.ntschy.underground.entity.base.FileDec;
import com.ntschy.underground.enums.ShapeType;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class AddProjectRequest {

    private String projectScope;

    private String projectName;

    private String createTime;

    private String createUser;

    @NotBlank(message = "guid不能为空")
    private String guid;

    // 点信息
    List<ShapePoint> points;

    @NotNull(message = "形状类型不能为空")
    private ShapeType shapeType;

    private List<FileDec> fileNames;
}
