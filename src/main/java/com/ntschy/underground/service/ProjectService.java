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

import com.ntschy.underground.entity.base.PageQuery;
import com.ntschy.underground.entity.base.Result;
import com.ntschy.underground.entity.dto.*;
import com.ntschy.underground.entity.vo.InspectionVO;
import com.ntschy.underground.entity.vo.RectificationVO;
import org.springframework.web.multipart.MultipartFile;

public interface ProjectService {
    // 新增项目
    Result addProject(AddProjectRequest addProjectRequest) throws RuntimeException;

    // 新增巡检
    Result addInspection(AddInspectionRequest addInspectionRequest, MultipartFile[] files) throws RuntimeException;

    // 新增整改
    Result addRectification(AddRectificationRequest addRectificationRequest) throws RuntimeException;

    // 获取项目列表
    Result getProjectList() throws RuntimeException;

    // 根据guid获取项目详情
    Result getProjectInfo(String guid) throws RuntimeException;

    // 增加项目图纸
    Result addProjectFiles(AddProjectFileRequest addProjectFileRequest) throws RuntimeException;

    // 查询巡检记录，pad用，不分页
    Result getInspectionList(QueryInspectionRequest queryInspectionRequest) throws RuntimeException;

    // 查询巡检记录
    PageQuery getInspectionPage(QueryInspectionRequest queryInspectionRequest) throws RuntimeException;

    // 获取巡检详情
    Result getInspectionInfo(InspectionVO inspectionVO) throws RuntimeException;

    // 获取整改详情
    Result getRectificationInfo(RectificationVO rectificationVO) throws RuntimeException;

    // 审阅巡检
    Result reviewInspection(ReviewInspectionRequest reviewInspectionRequest) throws RuntimeException;
}
