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
import com.ntschy.underground.entity.DO.RectificationRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ProjectDao {
    // 新增项目
    void addProject(ProjectRecord projectRecord) throws RuntimeException;

    // 新增巡检
    void addInspection(InspectionRecord inspectionRecord) throws RuntimeException;

    // 新增整改
    void addRectification(RectificationRecord rectificationRecord) throws RuntimeException;

    // 图片文件名插入到FILE_UPLOAD表
    void addFiles(@Param("type") Integer type,
                  @Param("businessId") String businessId,
                  @Param("fileNames") List<String> fileNames) throws RuntimeException;

    // 根据guid获取项目详情
    ProjectRecord getProjectInfo(@Param("guid") String guid);

    // 获取图片
    List<String> getFiles(@Param("type") Integer type,
                          @Param("businessId") String businessId) throws RuntimeException;
}
