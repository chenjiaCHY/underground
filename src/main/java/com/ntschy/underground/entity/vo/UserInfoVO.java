package com.ntschy.underground.entity.vo;

import lombok.Data;

@Data
public class UserInfoVO {

    private String userId;

    private String account;

    private String name;

    private String sex;

    private String phone;

    private String department;

    private String roleId;

    private Integer status;
}
