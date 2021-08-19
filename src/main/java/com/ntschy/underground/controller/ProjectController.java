/*
 * Copyright (c) 2021. All Rights Reserved.
 * ProjectName: underground
 * FileName: ProjectController.java
 * Author: 陈佳
 * Date: 2021/8/19 下午3:36
 * Version: 1.0
 * LastModified
 *
 */

package com.ntschy.underground.controller;

import com.ntschy.underground.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/project")
public class ProjectController {

    @Autowired
    private ProjectService projectService;


}
