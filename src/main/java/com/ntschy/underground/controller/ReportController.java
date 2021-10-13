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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@RestController
@RequestMapping("/report")
public class ReportController {

    @Autowired
    private ReportService reportService;
//    @RequestMapping("/getShapeFile")
//    public void getShapeFile(HttpServletRequest request, HttpServletResponse response, JSONArray json) {
//
//        try {
//
//
//            ShapeUtil.data2shape("", "", json);
//
//            // 下载
//            File file = new File("");
//            // 取得文件名
//            String fileName = file.getName();
//            // 以流的形式下载文件
//            InputStream fis = new BufferedInputStream(new FileInputStream(file));
//            byte[] buffer = new byte[fis.available()];
//            fis.read(buffer);
//            fis.close();
//            // 清空response
//            response.reset();
//            // 设置response的Header
//            response.addHeader("Content-Disposition", "attachment;filename=" + new String(fileName.getBytes(), "ISO8859-1"));
//            response.addHeader("Content-Length", "" + file.length());
//            OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
//            response.setContentType("application/octet-stream");
//            toClient.write(buffer);
//            toClient.flush();
//            toClient.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

//    @RequestMapping("/shapeFileToJson")
//    public Object shapeFileToJson() {
//
//        File file = new File("D:\\bitPoint.shp");
//
//        System.out.println(file.getName());
//        return ShapeUtil.shape2geoJson(file);
//    }

    @PostMapping("/downloadDXF")
    public void downloadDXF(@RequestBody @Validated DownloadDxfRequest downloadDxfRequest, HttpServletResponse response) throws Exception {

        String dxfText = reportService.generateDXF(downloadDxfRequest.getTables(), downloadDxfRequest.getPoints());

        response.setHeader("Content-Type", "application/octet-stream");
        response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode("开发区管线.dxf", "UTF-8"));
        OutputStream fileOutputStream = response.getOutputStream();

        fileOutputStream.write(dxfText.getBytes(StandardCharsets.UTF_8));

        fileOutputStream.flush();
        fileOutputStream.close();

    }

}
