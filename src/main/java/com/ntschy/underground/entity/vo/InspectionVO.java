/*
 * Copyright (c) 2021. All Rights Reserved.
 * ProjectName: underground
 * FileName: InspectionVO.java
 * Author: 陈佳
 * Date: 2021/8/25 下午4:07
 * Version: 1.0
 * LastModified
 *
 */

package com.ntschy.underground.entity.vo;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Data
public class InspectionVO {
    // 编号
    @NotBlank(message = "inspectionNumber不能为空")
    private String inspectionNumber;

    // 巡检id
    @NotBlank(message = "inspectionId不能为空")
    private String inspectionId;

    // 巡检日期
    private String createTime;

    // 巡检人
    private String inspector;

    // 联系电话
    private String phone;

    // 巡检类型
    private String type;

    // 所属项目
    private String projectName;

    // 地址
    private String address;

    // 问题描述
    private String description;

    // 整改进度
    private String progress;

    // pc端x坐标
    private String x;

    // pc端y坐标
    private String y;

    // 天地图x坐标
    private String xt;

    // 天地图y坐标
    private String yt;

    // 巡检意见
    private String RectifyComment;

    // 巡检照片
    private List<String> fileNames;

    // 整改记录
    private List<RectificationVO> rectificationList;
}
