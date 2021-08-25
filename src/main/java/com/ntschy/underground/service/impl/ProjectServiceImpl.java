/*
 * Copyright (c) 2021. All Rights Reserved.
 * ProjectName: underground
 * FileName: ProjectServiceImpl.java
 * Author: 陈佳
 * Date: 2021/8/19 下午2:58
 * Version: 1.0
 * LastModified
 *
 */

package com.ntschy.underground.service.impl;

import com.ntschy.underground.dao.ProjectDao;
import com.ntschy.underground.entity.DO.InspectionRecord;
import com.ntschy.underground.entity.DO.ProjectRecord;
import com.ntschy.underground.entity.DO.RectificationRecord;
import com.ntschy.underground.entity.base.Result;
import com.ntschy.underground.entity.dto.AddInspectionRequest;
import com.ntschy.underground.entity.dto.AddProjectRequest;
import com.ntschy.underground.entity.dto.AddRectificationRequest;
import com.ntschy.underground.enums.ProgressType;
import com.ntschy.underground.enums.UploadFileType;
import com.ntschy.underground.service.ProjectService;
import com.ntschy.underground.utils.Utils;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class ProjectServiceImpl implements ProjectService {

    @Resource
    private ProjectDao projectDao;


    @Override
    public Result getProjectInfo(String guid) {

        ProjectRecord projectRecord = projectDao.getProjectInfo(guid);

        if (!ObjectUtils.isEmpty(projectRecord)) {
            List<String> fileNames = projectDao.getFiles(UploadFileType.PROJECT.getCode(), projectRecord.getProjectId());
            projectRecord.setFileNames(fileNames);
        } else {
            return new Result(false, "获取项目信息失败!!!");
        }

        return new Result<>(projectRecord);

    }

    /**
     * 新建项目
     * @param addProjectRequest
     * @return
     * @throws RuntimeException
     */
    @Override
    public Result addProject(AddProjectRequest addProjectRequest) throws RuntimeException {

        String projectId = Utils.GenerateUUID(32);

        // 插入一条记录到PROJECT表中
        ProjectRecord projectRecord = new ProjectRecord();

        projectRecord.setProjectId(projectId);
        projectRecord.setProjectScope(addProjectRequest.getProjectScope());
        projectRecord.setProjectName(addProjectRequest.getProjectName());
        projectRecord.setCreateTime(addProjectRequest.getCreateTime());
        projectRecord.setCreateUser(addProjectRequest.getCreateUser());
        projectRecord.setGuid(addProjectRequest.getGuid());
        projectRecord.setShapeType(addProjectRequest.getShapeType().getCode());

        projectDao.addProject(projectRecord);

        // 将图纸文件名列表插入到FILE_UPLOAD表中
        projectDao.addFiles(UploadFileType.PROJECT.getCode(), projectId, addProjectRequest.getFileNames());

        return new Result(true, "新增项目成功!!!");
    }

    /**
     * 新增巡检
     * @param addInspectionRequest
     * @return
     * @throws RuntimeException
     */
    @Override
    public Result addInspection(AddInspectionRequest addInspectionRequest) throws RuntimeException {

        String inspectionId = Utils.GenerateUUID(32);

        SimpleDateFormat sdf = new SimpleDateFormat("yyMMddHHmmss");

        // 插入一条记录到INSPECTION表中
        InspectionRecord inspectionRecord = new InspectionRecord();

        inspectionRecord.setInspectionId(inspectionId);
        inspectionRecord.setCreateTime(sdf.format(new Date()));
        inspectionRecord.setInspector(addInspectionRequest.getInspector());
        inspectionRecord.setPhone(addInspectionRequest.getPhone());
        inspectionRecord.setType(addInspectionRequest.getInspectionType().getCode());
        inspectionRecord.setProjectName(addInspectionRequest.getProjectName());
        inspectionRecord.setAddress(addInspectionRequest.getAddress());
        inspectionRecord.setDescription(addInspectionRequest.getDescription());
        inspectionRecord.setProgress(ProgressType.NOT_REVIEW.getCode());

        projectDao.addInspection(inspectionRecord);

        // 将巡检照片文件名列表插入到FILE_UPLOAD表中
        projectDao.addFiles(UploadFileType.INSPECTION.getCode(), inspectionId, addInspectionRequest.getFileNames());

        return new Result(true, "新增巡检成功!!!");
    }

    /**
     * 新增整改
     * @param addRectificationRequest
     * @return
     * @throws RuntimeException
     */
    @Override
    public Result addRectification(AddRectificationRequest addRectificationRequest) throws RuntimeException {

        String rectificationId = Utils.GenerateUUID(32);

        SimpleDateFormat sdf = new SimpleDateFormat("yyMMddHHmmss");

        // 插入一条记录到INSPECTION表中
        RectificationRecord rectificationRecord = new RectificationRecord();

        rectificationRecord.setRectificationId(rectificationId);
        rectificationRecord.setCreateTime(sdf.format(new Date()));
        rectificationRecord.setInspectionId(addRectificationRequest.getInspectionId());
        rectificationRecord.setDescription(addRectificationRequest.getDescription());

        projectDao.addRectification(rectificationRecord);

        // 将巡检照片文件名列表插入到FILE_UPLOAD表中
        projectDao.addFiles(UploadFileType.RECTIFICATION.getCode(), rectificationId, addRectificationRequest.getFileNames());


        return new Result(true, "新增整改成功!!!");
    }

}
