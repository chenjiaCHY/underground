package com.ntschy.underground.entity.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class ModifyRoleRequest {

    private String roleID;

    private Integer status;

    private String roleName;

    private List<String> actionList;

    @NotNull(message = "操作类型不能为空")
    private Integer operateLabel;
}
