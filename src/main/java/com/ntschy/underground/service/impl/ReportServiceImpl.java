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

        String[] destPoints = points.split(",");

        StringBuilder innerSql = new StringBuilder();
        innerSql.append("( select st_geomfromtext('polygon((");
        for (int i = 0; i < destPoints.length - 1; i ++) {
            innerSql.append(destPoints[i]);

            if ((i + 1) % 2 == 0) {
                innerSql.append(",");
            }
            innerSql.append(" ");
        }

        innerSql.append(destPoints[destPoints.length - 1]);
        innerSql.append("))'))");

        DXFDocument dxfDocument = new DXFDocument("开发区管线");
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

                            dxfGraphics.drawLine(Double.valueOf(linePointList[0]),
                                    Double.valueOf(linePointList[1]),
                                    Double.valueOf(linePointList[2]),
                                    Double.valueOf(linePointList[3]));
                        } else if (geom.startsWith("POINT")) {
                            String pointPoints = geom.substring(6, geom.length() - 1);
                            String[] pointPointList = pointPoints.split(" ");

                            dxfGraphics.drawPoint(Double.valueOf(pointPointList[0]), Double.valueOf(pointPointList[1]));
                        } else if (geom.startsWith("MULTIPOLYGON")) {
                            String polygonPoints = geom.substring(15, geom.length() - 3);
                            polygonPoints = polygonPoints.replace(" ", ",");
                            String[] polygonPointList = polygonPoints.split(",");

                            Integer polygonSize = polygonPointList.length / 2 - 1;

                            double[] xList = new double[polygonSize];
                            double[] yList = new double[polygonSize];

                            for (int i = 0, j = 0; i < polygonSize; i++, j = j + 2) {
                                xList[i] = Double.valueOf(polygonPointList[j]);
                                yList[i] = Double.valueOf(polygonPointList[j + 1]);
                            }

                            dxfGraphics.drawPolygon(xList, yList, polygonSize);
                        }
                    }
                }
            }
        }

        String dxfText = dxfDocument.toDXFString();

        return dxfText;
    }
}
