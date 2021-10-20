/*
 * Copyright (c) 2021. All Rights Reserved.
 * ProjectName: underground
 * FileName: ReportController.java
 * Author: 陈佳
 * Date: 2021/9/15 上午9:35
 * Version: 1.0
 * LastModified
 */

package com.ntschy.underground.controller;

import com.ntschy.underground.entity.dto.DownloadDxfRequest;
import com.ntschy.underground.service.ReportService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;

@RestController
@RequestMapping("/report")
public class ReportController {

    @Value("${dxf.path}")
    private String dxfPath;

    @Autowired
    private ReportService reportService;

    @PostMapping("/downloadDXF")
    public void downloadDXF(@RequestBody @Validated DownloadDxfRequest downloadDxfRequest, HttpServletResponse response) throws Exception {

        String fileName = reportService.generateDXF(downloadDxfRequest.getTableNames(), downloadDxfRequest.getPoints());

        if (StringUtils.isBlank(fileName)) {
            throw new Exception("文件下载失败！");
        }

        File file = new File(dxfPath + fileName);

        if (file.exists()) {
            response.setHeader("Content-Type", "application/octet-stream");
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
            OutputStream fileOutputStream = response.getOutputStream();

            byte[] buffer = new byte[1024];
            FileInputStream fis = null;
            BufferedInputStream bis = null;
            try {
                fis = new FileInputStream(file);
                bis = new BufferedInputStream(fis);
                int i = bis.read(buffer);
                while (i != -1) {
                    fileOutputStream.write(buffer, 0, i);
                    i = bis.read(buffer);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (bis != null) {
                    try {
                        bis.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (fis != null) {
                    try {
                        fis.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

    }

}
