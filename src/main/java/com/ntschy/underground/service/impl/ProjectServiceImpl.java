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
import com.ntschy.underground.entity.base.Result;
import com.ntschy.underground.entity.dto.AddInspectionRequest;
import com.ntschy.underground.entity.dto.AddProjectRequest;
import com.ntschy.underground.entity.vo.ProjectInfoVO;
import com.ntschy.underground.enums.InspectionType;
import com.ntschy.underground.enums.ProgressType;
import com.ntschy.underground.enums.UploadFileType;
import com.ntschy.underground.service.ProjectService;
import com.ntschy.underground.utils.Utils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class ProjectServiceImpl implements ProjectService {

    @Resource
    private ProjectDao projectDao;


    @Override
    public ProjectInfoVO getProjectInfo(String projectId) {

        return projectDao.getProjectInfo(projectId);

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

        // 插入一条记录到INSPECTION表中
        InspectionRecord inspectionRecord = new InspectionRecord();

        inspectionRecord.setInspectionId(inspectionId);
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

}
