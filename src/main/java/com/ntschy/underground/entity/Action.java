package com.ntschy.underground.entity;

import lombok.Data;

import java.util.Date;

@Data
public class Action {

    private String id;

    private String name;

    private Integer sysId;

    private Integer moduleId;

    private Integer pageId;

    private String url;

    private String description;

    private Date createTime;

    private String createUser;

    private Integer status;
}
