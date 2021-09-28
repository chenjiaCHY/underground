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

import com.alibaba.fastjson.JSONArray;
import com.ntschy.underground.utils.ShapeUtil;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

@RestController
@RequestMapping("/report")
public class ReportController {


    @RequestMapping("/getShapeFile")
    public void getShapeFile(HttpServletRequest request, HttpServletResponse response, JSONArray json) {

        try {


            ShapeUtil.data2shape("", "", json);

            // 下载
            File file = new File("");
            // 取得文件名
            String fileName = file.getName();
            // 以流的形式下载文件
            InputStream fis = new BufferedInputStream(new FileInputStream(file));
            byte[] buffer = new byte[fis.available()];
            fis.read(buffer);
            fis.close();
            // 清空response
            response.reset();
            // 设置response的Header
            response.addHeader("Content-Disposition", "attachment;filename=" + new String(fileName.getBytes(), "ISO8859-1"));
            response.addHeader("Content-Length", "" + file.length());
            OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
            response.setContentType("application/octet-stream");
            toClient.write(buffer);
            toClient.flush();
            toClient.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RequestMapping("/shapeFileToJson")
    public Object shapeFileToJson() {

        File file = new File("D:\\bitPoint.shp");

        System.out.println(file.getName());
        return ShapeUtil.shape2geoJson(file);
    }

}
