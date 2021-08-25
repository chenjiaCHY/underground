/*
 * Copyright (c) 2021. All Rights Reserved.
 * ProjectName: underground
 * FileName: RectificationVO.java
 * Author: 陈佳
 * Date: 2021/8/25 下午4:31
 * Version: 1.0
 * LastModified
 *
 */

package com.ntschy.underground.entity.vo;

import lombok.Data;

import java.util.List;

@Data
public class RectificationVO {

    // 编号
    private String rectificationNumber;

    // 整改id
    private String rectificationId;

    // 整改时间
    private String createTime;

    // 巡检id
    private String inspectionId;

    // 整改情况描述
    private String description;

    // 整改人
    private String rectifyUser;

    // 整改照片
    private List<String> fileNames;
}
