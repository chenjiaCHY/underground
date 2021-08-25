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

@Data
public class QueryInspectionRequest {

    private ProgressType progress;

    private String createTime;

    private InspectionType type;
}
