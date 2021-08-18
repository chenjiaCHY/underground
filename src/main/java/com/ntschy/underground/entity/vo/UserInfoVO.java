package com.ntschy.underground.entity.vo;

import lombok.Data;

@Data
public class UserInfoVO {

    private String userID;

    private String account;

    private String name;

    private Integer sex;

    private String affiliation;

    private String affiliationID;

    private String phone;

    private String remark;

    private String roleID;

    private String roleName;

    private Integer status;
}
