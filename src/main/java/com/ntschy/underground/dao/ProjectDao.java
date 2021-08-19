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

import com.ntschy.underground.entity.vo.ProjectInfoVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ProjectDao {
    ProjectInfoVO getProjectInfo(@Param("projectId") String projectId);
}
