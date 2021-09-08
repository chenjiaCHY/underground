/*
 * Copyright (c) 2021. All Rights Reserved.
 * ProjectName: underground
 * FileName: ProjectPoints.java
 * Author: 陈佳
 * Date: 2021/9/8 下午4:03
 * Version: 1.0
 * LastModified
 */

package com.ntschy.underground.entity.DO;

import lombok.Data;

@Data
public class ProjectPoint {
    private String pid;

    private String guid;

    private String x;

    private String y;

    private String xt;

    private String yt;

    private Integer index;

    private Integer geoType;


}
