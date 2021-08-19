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
import com.ntschy.underground.entity.vo.ProjectInfoVO;
import com.ntschy.underground.service.ProjectService;
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

}
