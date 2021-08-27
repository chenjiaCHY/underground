/*
 * Copyright (c) 2021. All Rights Reserved.
 * ProjectName: underground
 * FileName: ProgressType.java
 * Author: 陈佳
 * Date: 2021/8/25 上午11:25
 * Version: 1.0
 * LastModified
 *
 */

package com.ntschy.underground.enums;

public enum ProgressType {

    NOT_REVIEW(1, "未审阅"),
    NOT_RECTIFY(2, "未整改"),
    RECTIFYING(3, "正在整改"),
    DONE(4, "已完成");

    private int code;
    private String name;

    private ProgressType(int code, String name) {
        this.code = code;
        this.name = name;
    }

    public static String getName(int code) {
        for (ProgressType dt : ProgressType.values()) {
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
