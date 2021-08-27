/*
 * Copyright (c) 2021. All Rights Reserved.
 * ProjectName: underground
 * FileName: AddInspectionRequest.java
 * Author: 陈佳
 * Date: 2021/8/25 上午11:20
 * Version: 1.0
 * LastModified
 *
 */

package com.ntschy.underground.entity.dto;

import com.ntschy.underground.enums.InspectionType;
import lombok.Data;

import java.util.List;

@Data
public class AddInspectionRequest {

    private String inspector;

    private String phone;

    private InspectionType inspectionType;

    private String projectName;

    private String address;

    private String description;

    private String guid;

    private List<String> fileNames;
}
