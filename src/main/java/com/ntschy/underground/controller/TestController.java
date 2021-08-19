package com.ntschy.underground.controller;

import com.ntschy.underground.entity.base.Result;
import com.ntschy.underground.entity.vo.ProjectInfoVO;
import com.ntschy.underground.entity.vo.UserInfoVO;
import com.ntschy.underground.service.AuthorityService;
import com.ntschy.underground.service.ProjectService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("test")
public class TestController {

    private final AuthorityService authorityService;

    private final ProjectService projectService;

    public TestController(AuthorityService authorityService, ProjectService projectService) {
        this.authorityService = authorityService;
        this.projectService = projectService;
    }

    @GetMapping("/hello")
    @ResponseBody
    public Result hello(@RequestParam("userId") String userId) {
        UserInfoVO userInfoVO = authorityService.getUserInfo(userId);

        return new Result(userInfoVO);
    }

    @GetMapping("testProject")
    @ResponseBody
    public Result testProject(@RequestParam("projectId") String projectId) {
        ProjectInfoVO projectInfoVO = projectService.getProjectInfo(projectId);
        return new Result(projectInfoVO);
    }


}
