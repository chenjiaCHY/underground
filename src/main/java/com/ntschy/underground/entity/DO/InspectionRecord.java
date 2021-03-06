/*
 * Copyright (c) 2021. All Rights Reserved.
 * ProjectName: underground
 * FileName: InspectionRecord.java
 * Author: 陈佳
 * Date: 2021/8/25 上午10:47
 * Version: 1.0
 * LastModified
 *
 */

package com.ntschy.underground.entity.DO;

import lombok.Data;

import java.util.List;

@Data
public class InspectionRecord {

    private String inspectionId;

    private String createTime;

    private String inspector;

    private String phone;

    private Integer type;

    private String projectName;

    private String address;

    private String description;

    private Integer progress;

    private String rectifyComment;

    private String sort;

    private String xt;

    private String yt;

    private String x;

    private String y;

    private List<String> fileNames;
}
