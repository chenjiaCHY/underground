/*
 * Copyright (c) 2021. All Rights Reserved.
 * ProjectName: underground
 * FileName: AddRectificationRequest.java
 * Author: 陈佳
 * Date: 2021/8/25 下午2:08
 * Version: 1.0
 * LastModified
 *
 */

package com.ntschy.underground.entity.dto;

import lombok.Data;

import java.util.List;

@Data
public class AddRectificationRequest {

    private String inspectionId;

    private String description;

    private String rectifyUser;
}
