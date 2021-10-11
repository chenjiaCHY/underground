/*
 * Copyright (c) 2021. All Rights Reserved.
 * ProjectName: underground
 * FileName: GeoDao.java
 * Author: 陈佳
 * Date: 2021/9/8 上午10:24
 * Version: 1.0
 * LastModified
 */

package com.ntschy.underground.dao;

import com.ntschy.underground.datasource.annotation.DataSource;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface GeoDao {

    @DataSource("slave2")
    List<String> getGeom(@Param("tableName") String tableName, @Param("innerSQL") String innerSQL) throws RuntimeException;
}
