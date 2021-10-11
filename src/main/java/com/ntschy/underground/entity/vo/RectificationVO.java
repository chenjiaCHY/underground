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

import com.ntschy.underground.entity.base.FileDec;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Data
public class RectificationVO {

    // 编号
    @NotBlank(message = "rectificationNumber不能为空")
    private String rectificationNumber;

    // 整改id
    @NotBlank(message = "rectificationId不能为空")
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
