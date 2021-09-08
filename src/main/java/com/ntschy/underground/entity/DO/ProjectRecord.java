/*
 * Copyright (c) 2021. All Rights Reserved.
 * ProjectName: underground
 * FileName: ProjectRecord.java
 * Author: 陈佳
 * Date: 2021/8/25 上午10:43
 * Version: 1.0
 * LastModified
 *
 */

package com.ntschy.underground.entity.DO;

import lombok.Data;

import java.util.List;

@Data
public class ProjectRecord {

    private String projectId;

    private String projectScope;

    private String projectName;

    private String createTime;

    private String createUser;

    private String guid;

    private Integer shapeType;

    private List<ProjectPoint> points;

    private List<String> fileNames;
}
