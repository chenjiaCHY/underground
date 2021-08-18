package com.ntschy.underground.entity.vo;

import com.ntschy.underground.entity.base.PageInfo;
import lombok.Data;

import java.util.List;

@Data
public class RoleInfoVO {

    private String roleID;

    private String roleName;

    private Integer status;

    private List<PageInfo> pageInfos;
}
