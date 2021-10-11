/*
 * Copyright (c) 2021. All Rights Reserved.
 * ProjectName: underground
 * FileName: ReportService.java
 * Author: 陈佳
 * Date: 2021/10/11 下午2:56
 * Version: 1.0
 * LastModified
 */

package com.ntschy.underground.service;

import java.util.List;

public interface ReportService {
    // 生成DXF文本
    String generateDXF(List<String> tables, String points) throws RuntimeException;
}
