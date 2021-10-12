/*
 * Copyright (c) 2021. All Rights Reserved.
 * ProjectName: underground
 * FileName: ReportServiceImpl.java
 * Author: 陈佳
 * Date: 2021/10/11 下午2:58
 * Version: 1.0
 * LastModified
 */

package com.ntschy.underground.service.impl;

import com.jsevy.jdxf.DXFDocument;
import com.jsevy.jdxf.DXFGraphics;
import com.ntschy.underground.dao.GeoDao;
import com.ntschy.underground.service.ReportService;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.awt.*;
import java.util.List;

@Service
public class ReportServiceImpl implements ReportService {

    @Resource
    private GeoDao geoDao;

    @Override
    public String generateDXF(List<String> tables, String points) {

        String[] polygonPoints = points.split(",");

        // 拼装sql并获取最小的X和Y
        Double xMin = 0.0;
        Double yMin = 0.0;
        StringBuilder innerSql = new StringBuilder();
        innerSql.append("( select st_geomfromtext('polygon((");
        for (int i = 0; i < polygonPoints.length - 1; i ++) {
            innerSql.append(polygonPoints[i]);

            if (i == 0) {
                xMin = Double.valueOf(polygonPoints[i]);
            }
            if (i == 1) {
                yMin = Double.valueOf(polygonPoints[i]);
            }

            if ((i + 1) % 2 == 0) {
                innerSql.append(",");
                if (yMin > Double.valueOf(polygonPoints[i])) {
                    yMin = Double.valueOf(polygonPoints[i]);
                }
            } else {
                if (xMin > Double.valueOf(polygonPoints[i])) {
                    xMin = Double.valueOf(polygonPoints[i]);
                }
            }
            innerSql.append(" ");
        }

        innerSql.append(polygonPoints[polygonPoints.length - 1]);
        innerSql.append("))'))");

        DXFDocument dxfDocument = new DXFDocument("Example");
        DXFGraphics dxfGraphics = dxfDocument.getGraphics();

        dxfGraphics.setColor(Color.BLUE);
        dxfGraphics.setStroke(new BasicStroke(30));

        // 利用st_intersects函数先从表中找出对应的要素并写入dxf
        if (!CollectionUtils.isEmpty(tables)) {
            for (String tableName : tables) {
                List<String> geomList = geoDao.getGeom(tableName, innerSql.toString());

                if (!CollectionUtils.isEmpty(geomList)) {
                    for (String geom : geomList) {
                        if (geom.startsWith("MULTILINESTRING")) {
                            String linePoints = geom.substring(17, geom.length() - 2);
                            linePoints = linePoints.replace(" ", ",");

                            String[] linePointList = linePoints.split(",");

                            dxfGraphics.drawLine(Double.valueOf(linePointList[0]) - xMin,
                                    Double.valueOf(linePointList[1]) -yMin,
                                    Double.valueOf(linePointList[2]) - xMin,
                                    Double.valueOf(linePointList[3]) - yMin);
                        }

                        if (geom.startsWith("POINT")) {
                            String pointPoints = geom.substring(6, geom.length() - 1);
                            String[] pointPointList = pointPoints.split(" ");

                            dxfGraphics.drawPoint(Double.valueOf(pointPointList[0]) - xMin, Double.valueOf(pointPointList[1]) - yMin);
                        }
                    }
                }
            }
        }

        String dxfText = dxfDocument.toDXFString();

        return dxfText;
    }
}
