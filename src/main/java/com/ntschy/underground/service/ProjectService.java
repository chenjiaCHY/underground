/*
 * Copyright (c) 2021. All Rights Reserved.
 * ProjectName: underground
 * FileName: ProjectService.java
 * Author: 陈佳
 * Date: 2021/8/19 下午2:55
 * Version: 1.0
 * LastModified
 *
 */

package com.ntschy.underground.service;

import com.ntschy.underground.entity.base.Result;
import com.ntschy.underground.entity.dto.AddInspectionRequest;
import com.ntschy.underground.entity.dto.AddProjectRequest;
import com.ntschy.underground.entity.dto.AddRectificationRequest;

public interface ProjectService {
    // 新增项目
    Result addProject(AddProjectRequest addProjectRequest) throws RuntimeException;

    // 新增巡检
    Result addInspection(AddInspectionRequest addInspectionRequest) throws RuntimeException;

    // 新增整改
    Result addRectification(AddRectificationRequest addRectificationRequest) throws RuntimeException;

    // 根据guid获取项目详情
    Result getProjectInfo(String guid) throws RuntimeException;
}
