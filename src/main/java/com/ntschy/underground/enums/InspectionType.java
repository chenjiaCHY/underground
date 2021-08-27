/*
 * Copyright (c) 2021. All Rights Reserved.
 * ProjectName: underground
 * FileName: InspectionType.java
 * Author: 陈佳
 * Date: 2021/8/25 上午11:22
 * Version: 1.0
 * LastModified
 *
 */

package com.ntschy.underground.enums;

public enum  InspectionType {

    PROJECT(1, "项目巡检"),
    ROUTINE(2, "日常巡检");

    private int code;
    private String name;

    private InspectionType(int code, String name) {
        this.code = code;
        this.name = name;
    }

    public static String getName(int code) {
        for (InspectionType dt : InspectionType.values()) {
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
