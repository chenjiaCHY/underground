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

import com.ntschy.underground.entity.base.PageQuery;
import com.ntschy.underground.entity.base.Result;
import com.ntschy.underground.entity.dto.*;
import com.ntschy.underground.entity.vo.InspectionVO;
import com.ntschy.underground.entity.vo.RectificationVO;
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
    public Result addInspection(@Validated AddInspectionRequest addInspectionRequest, @RequestParam("files") MultipartFile[] files) {
        try {
            Result result = projectService.addInspection(addInspectionRequest, files);
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

    /**
     * 获取项目列表，平板端使用
     * @return
     */
    @GetMapping("/getProjectList")
    @ResponseBody
    public Result getProjectList() {
        try {
            Result result = projectService.getProjectList();
            return result;
        } catch (Exception e) {
            return new Result(false, e.getMessage());
        }
    }

    /**
     * 根据guid获取项目详情
     * @param guid
     * @return
     */
    @GetMapping("/getProjectInfo")
    @ResponseBody
    public Result getProjectInfo(@RequestParam("guid") String guid) {
        try {
            Result result = projectService.getProjectInfo(guid);
            return result;
        } catch (Exception e) {
            return new Result(false, e.getMessage());
        }
    }

    /**
     * 增加项目图纸
     * @param addProjectFileRequest
     * @return
     */
    @PostMapping("/addProjectFiles")
    @ResponseBody
    public Result addProjectFiles(@RequestBody @Validated AddProjectFileRequest addProjectFileRequest) {
        try {
            Result result = projectService.addProjectFiles(addProjectFileRequest);
            return result;
        } catch (Exception e) {
            return new Result(false, e.getMessage());
        }
    }

    /**
     * 查询所有巡检，无分页
     * @param queryInspectionRequest
     * @return
     */
    @PostMapping("/getInspectionList")
    @ResponseBody
    public Result getInspectionList(@RequestBody QueryInspectionRequest queryInspectionRequest) {
        try {
            Result result = projectService.getAllInspection(queryInspectionRequest);
            return result;
        } catch (Exception e) {
            return new Result(false, e.getMessage());
        }
    }

    /**
     * 查询巡检记录
     * @param queryInspectionRequest
     * @return
     */
    @PostMapping("/getInspectionPage")
    @ResponseBody
    public Result getInspectionPage(@RequestBody @Validated QueryInspectionRequest queryInspectionRequest) {
        try {
            PageQuery pageQuery = projectService.getInspectionList(queryInspectionRequest);
            return new Result<>(pageQuery);
        } catch (Exception e) {
            return new Result(false, e.getMessage());
        }
    }

    /**
     * 获取巡检详情
     * @param inspectionVO
     * @return
     */
    @PostMapping("/getInspectionInfo")
    @ResponseBody
    public Result getInspectionInfo(@RequestBody @Validated InspectionVO inspectionVO) {
        try {
            Result result = projectService.getInspectionInfo(inspectionVO);
            return result;
        } catch (Exception e) {
            return new Result(false, e.getMessage());
        }
    }

    /**
     * 获取整改详情
     * @param rectificationVO
     * @return
     */
    @PostMapping("/getRectificationInfo")
    @ResponseBody
    public Result getRectificationInfo(@RequestBody @Validated RectificationVO rectificationVO) {
        try {
            Result result = projectService.getRectificationInfo(rectificationVO);
            return result;
        } catch (Exception e) {
            return new Result(false, e.getMessage());
        }
    }

    /**
     * 审阅巡检
     * @param reviewInspectionRequest
     * @return
     */
    @PostMapping("/reviewInspection")
    @ResponseBody
    public Result reviewInspection(@RequestBody @Validated ReviewInspectionRequest reviewInspectionRequest) {
        try {
            Result result = projectService.reviewInspection(reviewInspectionRequest);
            return result;
        } catch (Exception e) {
            return new Result(false, e.getMessage());
        }
    }
}
