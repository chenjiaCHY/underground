/*
 * Copyright (c) 2021. All Rights Reserved.
 * ProjectName: underground
 * FileName: ProjectInfoVO.java
 * Author: 陈佳
 * Date: 2021/8/19 下午2:57
 * Version: 1.0
 * LastModified
 *
 */

package com.ntschy.underground.entity.vo;

import lombok.Data;

@Data
public class ProjectInfoVO {
    private String projectId;

    private String scope;

    private String name;

    private String createTime;

    private String createUser;
}
