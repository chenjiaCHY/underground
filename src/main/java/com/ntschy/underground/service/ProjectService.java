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

import com.ntschy.underground.entity.vo.ProjectInfoVO;

public interface ProjectService {
    ProjectInfoVO getProjectInfo(String projectId);
}
