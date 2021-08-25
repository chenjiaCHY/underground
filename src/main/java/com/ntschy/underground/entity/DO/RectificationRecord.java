/*
 * Copyright (c) 2021. All Rights Reserved.
 * ProjectName: underground
 * FileName: RectificationRecord.java
 * Author: 陈佳
 * Date: 2021/8/25 上午10:50
 * Version: 1.0
 * LastModified
 *
 */

package com.ntschy.underground.entity.DO;

import lombok.Data;

import java.util.List;

@Data
public class RectificationRecord {

    private Integer id;

    private String rectificationId;

    private String inspectionId;

    private String description;

    private List<String> fileNames;
}
