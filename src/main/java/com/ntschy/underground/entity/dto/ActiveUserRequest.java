/*
 * Copyright (c) 2021. All Rights Reserved.
 * ProjectName: underground
 * FileName: ActiveUserRequest.java
 * Author: 陈佳
 * Date: 2021/8/23 上午10:12
 * Version: 1.0
 * LastModified
 *
 */

package com.ntschy.underground.entity.dto;

import lombok.Data;

import java.util.List;

@Data
public class ActiveUserRequest {

    private List<String> userIdList;

    private Integer status;
}
