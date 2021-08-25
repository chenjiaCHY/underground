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

import com.ntschy.underground.entity.base.Result;
import com.ntschy.underground.entity.dto.AddInspectionRequest;
import com.ntschy.underground.entity.dto.AddProjectRequest;
import com.ntschy.underground.entity.dto.AddRectificationRequest;
import com.ntschy.underground.service.ProjectService;
import com.ntschy.underground.utils.ToolUpload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/project")
@Validated
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    @Value("${file.uploadPath}")
    private String uploadPath;

    /**
     * 上传照片
     * @param file
     * @return
     */
    @PostMapping("/uploadImage")
    @ResponseBody
    public Result uploadImage(@RequestParam("file") MultipartFile file) {
        Result result = ToolUpload.fileUpload(file, uploadPath);
        return result;
    }

    /**
     * 新增项目
     * @param addProjectRequest
     * @return
     */
    @PostMapping("/addProject")
    @ResponseBody
    public Result addProject(@RequestBody @Validated AddProjectRequest addProjectRequest) {
        try {
            Result result = projectService.addProject(addProjectRequest);
            return result;
        } catch (Exception e) {
            return new Result(false, e.getMessage());
        }
    }

    /**
     * 新增巡检
     * @param addInspectionRequest
     * @return
     */
    @PostMapping("/addInspection")
    @ResponseBody
    public Result addInspection(@RequestBody @Validated AddInspectionRequest addInspectionRequest) {
        try {
            Result result = projectService.addInspection(addInspectionRequest);
            return result;
        } catch (Exception e) {
            return new Result(false, e.getMessage());
        }
    }

    /**
     * 新增整改记录
     * @param addRectificationRequest
     * @return
     */
    @PostMapping("/addRectification")
    @ResponseBody
    public Result addRectification(@RequestBody @Validated AddRectificationRequest addRectificationRequest) {
        try {
            Result result = projectService.addRectification(addRectificationRequest);
            return result;
        } catch (Exception e) {
            return new Result(false, e.getMessage());
        }
    }


}
