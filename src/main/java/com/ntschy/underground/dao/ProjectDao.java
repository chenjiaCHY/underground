/*
 * Copyright (c) 2021. All Rights Reserved.
 * ProjectName: underground
 * FileName: ProjectDao.java
 * Author: 陈佳
 * Date: 2021/8/19 下午2:59
 * Version: 1.0
 * LastModified
 *
 */

package com.ntschy.underground.dao;

import com.ntschy.underground.entity.DO.InspectionRecord;
import com.ntschy.underground.entity.DO.ProjectRecord;
import com.ntschy.underground.entity.vo.ProjectInfoVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ProjectDao {
    ProjectInfoVO getProjectInfo(@Param("projectId") String projectId);

    // 新增项目
    void addProject(ProjectRecord projectRecord) throws RuntimeException;

    // 新增巡检
    void addInspection(InspectionRecord inspectionRecord) throws RuntimeException;

    // 图片文件名插入到FILE_UPLOAD表
    void addFiles(@Param("type") Integer type,
                  @Param("businessId") String businessId,
                  @Param("fileNames") List<String> fileNames) throws RuntimeException;


}
