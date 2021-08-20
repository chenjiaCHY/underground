/*
 * Copyright (c) 2021. All Rights Reserved.
 * ProjectName: underground
 * FileName: AuthorityController.java
 * Author: 陈佳
 * Date: 2021/8/19 下午3:30
 * Version: 1.0
 * LastModified
 *
 */

package com.ntschy.underground.controller;

import com.ntschy.underground.entity.base.Result;
import com.ntschy.underground.entity.dto.ModifyRoleRequest;
import com.ntschy.underground.entity.dto.ModifyUserRequest;
import com.ntschy.underground.entity.dto.UserLogin;
import com.ntschy.underground.service.AuthorityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequestMapping("/authority")
@Validated
public class AuthorityController {

    @Autowired
    private AuthorityService authorityService;

    /**
     * 用户登录
     * @param userLogin
     * @return
     */
    @PostMapping("/login")
    @ResponseBody
    public Result userLogin(@RequestBody @Validated UserLogin userLogin) {
        try {
            Map<String, Object> map = authorityService.userLogin(userLogin);
            return new Result(map);
        } catch (Exception e) {
            return new Result(false, e.getMessage());
        }
    }

    /**
     * 对角色进行操作
     * 1：新增
     * 2：修改
     * 3：删除
     * @return
     */
    @PostMapping("/roleModify")
    @ResponseBody
    public Result roleModify(@RequestBody @Validated ModifyRoleRequest modifyRoleRequest) {
        try {
            Result result = authorityService.roleModify(modifyRoleRequest);
            return result;
        } catch (Exception e) {
            return new Result(false, e.getMessage());
        }
    }

    /**
     * 对用户进行操作
     * 1：新增
     * 2：修改
     * 3：删除
     * @return
     */
    @PostMapping("/userModify")
    @ResponseBody
    public Result userModify(@RequestBody @Validated ModifyUserRequest modifyUserRequest) {
        try {
            Result result = authorityService.userModify(modifyUserRequest);
            return result;
        } catch (Exception e) {
            return new Result(false, e.getMessage());
        }
    }

    /**
     * 获取角色列表，分页
     * @return
     */
    @PostMapping("/getRoleList")
    @ResponseBody
    public Result getRoleList() {
        return null;
    }

    /**
     * 获取用户列表，分页
     * @return
     */
    @PostMapping("/getUserList")
    @ResponseBody
    public Result getUserList() {
        return null;
    }

    /**
     * 获取所有角色，新增用户时选择角色用
     * @return
     */
    @GetMapping("/getFullRoleList")
    @ResponseBody
    public Result getFullRoleList() {
        return null;
    }

    /**
     * 获取角色详情
     * @return
     */
    @GetMapping("/getRoleInfo")
    @ResponseBody
    public Result getRoleInfo() {
        return null;
    }

    /**
     * 获取用户详情
     * @return
     */
    @GetMapping("/getUserInfo")
    @ResponseBody
    public Result getUserInfo() {
        return null;
    }

    /**
     * 修改密码
     * @return
     */
    @PostMapping("/modifyUserPwd")
    @ResponseBody
    public Result modifyUserPwd() {
        return null;
    }

}
