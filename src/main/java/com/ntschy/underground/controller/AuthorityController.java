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
import com.ntschy.underground.entity.dto.UserLogin;
import com.ntschy.underground.service.AuthorityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

@Controller
@RequestMapping("/authority")
@Validated
public class AuthorityController {

    @Autowired
    private AuthorityService authorityService;


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
}
