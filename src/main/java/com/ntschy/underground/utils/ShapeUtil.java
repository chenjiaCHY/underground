/*
 * Copyright (c) 2021. All Rights Reserved.
 * ProjectName: underground
 * FileName: ShapeUtil.java
 * Author: 陈佳
 * Date: 2021/9/13 上午9:34
 * Version: 1.0
 * LastModified
 */

package com.ntschy.underground.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.geotools.data.FeatureWriter;
import org.geotools.data.Transaction;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.data.shapefile.ShapefileDataStoreFactory;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.geojson.feature.FeatureJSON;
import org.geotools.geojson.geom.GeometryJSON;
import org.geotools.geometry.jts.JTSFactoryFinder;
import org.locationtech.jts.geom.*;
import org.locationtech.jts.io.WKTReader;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;


import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;


public class ShapeUtil {

    public static JSONObject shape2geoJson(File shpFile) {
        // 获取文件前缀名
        String fileName = shpFile.getName();
        String layerName = fileName.substring(0, fileName.lastIndexOf("."));

        ShapefileDataStore dataStore = buildDataStore(shpFile);
//        dataStore.setCharset(Charset.forName("GBK"));
        StringBuffer sb = new StringBuffer();
        FeatureJSON fJson = new FeatureJSON();
        JSONObject result = new JSONObject();
        JSONObject geoJson = new JSONObject();
        JSONArray features = new JSONArray();
        result.put("name", layerName);
        result.put("data", geoJson);
        geoJson.put("type", "FeatureCollection");
        try {
            String typeName = dataStore.getTypeNames()[0];
            SimpleFeatureSource featureSource = dataStore.getFeatureSource(typeName);
            SimpleFeatureCollection collection = featureSource.getFeatures();
            SimpleFeatureIterator iterator = collection.features();
            while (iterator.hasNext()) {
                SimpleFeature feature = iterator.next();
                StringWriter writer = new StringWriter();
                fJson.writeFeature(feature, writer);
                JSONObject json = JSONObject.parseObject(writer.toString());
                features.add(json);
            }
            geoJson.put("features", features);
            iterator.close();
            sb.append(geoJson);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            dataStore.dispose();
        }
        return result;
    }

    public static ShapefileDataStore buildDataStore(String shpFilePath) {
        return buildDataStore(new File(shpFilePath));
    }

    public static ShapefileDataStore buildDataStore(File shpFile) {
        ShapefileDataStoreFactory factory = new ShapefileDataStoreFactory();
        try {
            ShapefileDataStore dataStore = (ShapefileDataStore) factory.createDataStore(shpFile.toURI().toURL());
            if (dataStore != null) {
                dataStore.setCharset(Charset.forName("GBK"));
            }
            return dataStore;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

//    public static void data2Shape(String dirPath, TableInfo)

    public static void data2shape(String zipPath, String srcDirName, JSONArray json) {
        String geometryName = "the_geom";
        // 空间信息解析器
        GeometryFactory geometryFactory = JTSFactoryFinder.getGeometryFactory();
        WKTReader wktReader = new WKTReader(geometryFactory);
        GeometryJSON geometryJSON = new GeometryJSON();
        try {
            // 步骤一 创建shape文件对象，使用工厂函数生成datastore
            String fileDir = zipPath + File.separator + srcDirName;
            FileUtils.makeFileDir(zipPath);
            FileUtils.makeFileDir(fileDir);
            String filePath = fileDir + File.separator + "station.shp";
            File shapeFile = new File(filePath);
            if (shapeFile.exists()) {
                if (!shapeFile.createNewFile()) {
                    throw new RuntimeException("无法创建shape文件");
                }
            }
            ShapefileDataStore shpDataStore = (ShapefileDataStore) new ShapefileDataStoreFactory().createDataStore(shapeFile.toURI().toURL());

            // 步骤二 创建shp文件的结构，也就是schema，告诉他你这个文件有哪些属性， 是点线还是面。
            SimpleFeatureTypeBuilder typeBuilder = new SimpleFeatureTypeBuilder();
            //typeBuilder.setCRS(DefaultGeographicCRS.WGS84);//坐标系
            typeBuilder.setName("station");
            typeBuilder.add(geometryName, Point.class);
            JSONObject jsonObject = json.getJSONObject(0);
            Set<String> keys = jsonObject.keySet();
            for (String key : keys) {
                Object value = jsonObject.keySet();
                if (value instanceof  Integer) {
                    typeBuilder.add(key, Integer.class);
                } else if(value instanceof String) {
                    typeBuilder.add(key, String.class);
                } else if (value instanceof Double || value instanceof BigDecimal || value instanceof Float) {
                    typeBuilder.add(key, Double.class);
                }
            }

            SimpleFeatureType featureType = typeBuilder.buildFeatureType();
            shpDataStore.createSchema(featureType);

            // 步骤三 有了schema下一步就要生成一个个的feature了
            SimpleFeatureBuilder featureBuilder = new SimpleFeatureBuilder(featureType);
            List<SimpleFeature> features = new ArrayList<>();
            for (int i = 0, len = json.size(); i < len; i++) {
                JSONObject data = json.getJSONObject(i);
                // 设置空间属性
                Double longitude = data.getDouble("LONGITUDE");
                Double latitude = data.getDouble("LATITUDE");
                Point poin = geometryFactory.createPoint(new Coordinate(longitude, latitude));
                // 设置其他属性
                Set<String> keyset = data.keySet();
                for (String key : keyset) {
                    Object value = data.get(key);
                    if (value instanceof String) {
                        value = new String(value.toString().getBytes(), "ISO-8859-1");
                    }
                    featureBuilder.add(value);
                }
                features.add(featureBuilder.buildFeature(null));
            }

            // 步骤四 把生成的feature写入文件
            FeatureWriter<SimpleFeatureType, SimpleFeature> featureWriter = shpDataStore.getFeatureWriter(shpDataStore.getTypeNames()[0], Transaction.AUTO_COMMIT);
            for (int i = 0, len = features.size(); i < len; i++) {
                // 获取要写入的要素
                SimpleFeature feature = features.get(i);
                // 将要写入位置
                SimpleFeature featureBuf = featureWriter.next();
                // 设置写入要素所有属性
                featureBuf.setAttributes(feature.getAttributes());
            }
            featureWriter.write();

            //5 释放资源
            featureWriter.close();
            shpDataStore.dispose();

            FileUtils.createFile(fileDir + File.separator + "station.cpg", "OEM");

            // 压缩源文件（夹）路径，保存的路径，保存的文件名
            ZipUtil.zip(fileDir, zipPath, srcDirName + ".zip");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static JSONObject zip2geoJson(String zipFilePath, String unzipDir) {
        try {
            String dirName = zipFilePath.substring(0, zipFilePath.lastIndexOf("."));
            ZipUtil.unzip(zipFilePath, unzipDir, true);
            List<File> shapeFiles = new ArrayList<>();
            findFileBySuffix(new File(dirName), shapeFiles);
            if (shapeFiles.size() != 1) {
                throw new RuntimeException("shape压缩文件错误");
            }
            File shapeFile = shapeFiles.get(0);
            return shape2geoJson(shapeFile);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // 递归获得文件夹下所有的shp后缀文件
    public static void findFileBySuffix(File file, List<File> resultList) {
        // 判断是否为文件夹
        if (file.isDirectory()) {
            File[] listFiles = file.listFiles();
            // 执行操作
            for (File f : listFiles) {
                findFileBySuffix(f, resultList);
            }
        } else if (file.getName().endsWith(".shp")) {
            resultList.add(file);
        }
    }
}
