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

import com.ntschy.underground.dao.GeoDao;
import com.ntschy.underground.service.ReportService;
import org.apache.cxf.endpoint.Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.xml.namespace.QName;

@Service
public class ReportServiceImpl implements ReportService {

    @Resource
    private GeoDao geoDao;

    @Value("${webservice.namespace}")
    private String nameSpace;

    @Value("${webservice.exportdxf}")
    private String exportdxf;

    @Autowired
    Client client;

    @Override
    public String generateDXF(String tableNames, String points) {

        String fileName = "";

        try {
            QName qName = new QName(nameSpace, exportdxf);
            Object[] objects = client.invoke(qName, tableNames, points);

            fileName = objects[0].toString();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return fileName;
    }
}
