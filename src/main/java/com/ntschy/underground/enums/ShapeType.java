/*
 * Copyright (c) 2021. All Rights Reserved.
 * ProjectName: underground
 * FileName: ShapeType.java
 * Author: 陈佳
 * Date: 2021/8/25 上午9:59
 * Version: 1.0
 * LastModified
 *
 */

package com.ntschy.underground.enums;

public enum  ShapeType {

    POINT(1, "点"),
    LINE(2, "线"),
    POLYGON(3, "面");

    private int code;
    private String name;

    private ShapeType(int code, String name) {
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
