/*
 * Copyright (c) 2021. All Rights Reserved.
 * ProjectName: underground
 * FileName: UploadFileType.java
 * Author: 陈佳
 * Date: 2021/8/25 上午9:21
 * Version: 1.0
 * LastModified
 *
 */

package com.ntschy.underground.enums;

public enum UploadFileType {

    PROJECT(1, "项目"),
    INSPECTION(2, "巡检"),
    RECTIFICATION(3, "整改");

    private int code;
    private String name;

    private UploadFileType(int code, String name) {
        this.code = code;
        this.name = name;
    }

    public static String getName(int code) {
        for (UploadFileType dt : UploadFileType.values()) {
            if (dt.getCode() == code) {
                return dt.getName();
            }
        }
        return null;
    }

    public int getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public void setName(String name) {
        this.name = name;
    }
}
