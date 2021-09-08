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

import com.ntschy.underground.datasource.annotation.DataSource;
import com.ntschy.underground.entity.DO.InspectionRecord;
import com.ntschy.underground.entity.DO.ProjectPoint;
import com.ntschy.underground.entity.DO.ProjectRecord;
import com.ntschy.underground.entity.DO.RectificationRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ProjectDao {
    // 新增项目
    @DataSource("slave1")
    void addProject(ProjectRecord projectRecord) throws RuntimeException;

    // 新增巡检
    @DataSource("slave1")
    void addInspection(InspectionRecord inspectionRecord) throws RuntimeException;

    // 新增整改
    @DataSource("slave1")
    void addRectification(RectificationRecord rectificationRecord) throws RuntimeException;

    // 图片文件名插入到FILE_UPLOAD表
    @DataSource("slave1")
    void addFiles(@Param("type") Integer type,
                  @Param("businessId") String businessId,
                  @Param("fileNames") List<String> fileNames) throws RuntimeException;

    // 从FILE_UPLOAD表删除对应的文件
    @DataSource("slave1")
    void deleteFiles(@Param("type") Integer type,
                     @Param("businessId") String businessId) throws RuntimeException;

    // 获取项目列表
    @DataSource("slave1")
    List<ProjectRecord> getProjectList() throws RuntimeException;

    // 根据guid获取项目详情
    @DataSource("slave1")
    ProjectRecord getProjectInfo(@Param("guid") String guid);

    // 获取图片
    @DataSource("slave1")
    List<String> getFiles(@Param("type") Integer type,
                          @Param("businessId") String businessId) throws RuntimeException;

    // 查询巡检总数
    @DataSource("slave1")
    Integer getInspectionCount(@Param("progress") Integer progress,
                               @Param("createTime") String createTime,
                               @Param("type") Integer type) throws RuntimeException;

    // 查询巡检记录
    @DataSource("slave1")
    List<InspectionRecord> getInspectionList(@Param("progress") Integer progress,
                                             @Param("createTime") String createTime,
                                             @Param("type") Integer type,
                                             @Param("startNo") Integer startNo,
                                             @Param("endNo") Integer endNo) throws RuntimeException;

    // 根据Sort字段查询区间内数量
    @DataSource("slave1")
    Integer findCountBySort(@Param("beginSort") String beginSort,
                            @Param("endSort") String endSort) throws RuntimeException;

    // 获取整改列表
    @DataSource("slave1")
    List<RectificationRecord> getRectificationList(@Param("inspectionId") String inspectionId) throws RuntimeException;

    // 审阅巡检
    @DataSource("slave1")
    void updateInspection(@Param("inspectionId") String inspectionId,
                          @Param("progress") Integer progress,
                          @Param("rectifyComment") String rectifyComment) throws RuntimeException;

    // 通过guid查询巡检详情
    @DataSource("slave1")
    InspectionRecord getInspectionByGuid(@Param("guid") String guid) throws RuntimeException;

    // 查询所有巡检记录，不分页
    @DataSource("slave1")
    List<InspectionRecord> getAllInspection(@Param("progress") Integer progress,
                                            @Param("createTime") String createTime,
                                            @Param("type") Integer type) throws RuntimeException;

    // 坐标点转换后插入到表中
    @DataSource("slave1")
    void insertProjectPoints(@Param("pointList") List<ProjectPoint> pointList) throws RuntimeException;
}
