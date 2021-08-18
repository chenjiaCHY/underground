package com.ntschy.underground.service.impl;

import com.ntschy.underground.dao.AuthorityDao;
import com.ntschy.underground.entity.base.LoginUserPwd;
import com.ntschy.underground.entity.base.OperationLog;
import com.ntschy.underground.entity.base.PageInfo;
import com.ntschy.underground.entity.base.PageQuery;
import com.ntschy.underground.entity.dto.ModifyRoleRequest;
import com.ntschy.underground.entity.dto.ModifyUserRequest;
import com.ntschy.underground.entity.dto.QueryRoleRequest;
import com.ntschy.underground.entity.dto.QueryUserRequest;
import com.ntschy.underground.entity.vo.LoginToken;
import com.ntschy.underground.entity.vo.RoleInfoVO;
import com.ntschy.underground.entity.vo.UserInfoVO;
import com.ntschy.underground.entity.vo.UserLogin;
import com.ntschy.underground.service.AuthorityService;
import org.apache.catalina.User;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class AuthorityServiceImpl implements AuthorityService {

    @Resource
    private AuthorityDao authorityDao;

    @Override
    public PageQuery getRoleList(QueryRoleRequest queryRoleRequest) {
        return new PageQuery(1, 1, 10, null);
    }

    @Override
    public PageQuery getUserList(QueryUserRequest queryUserRequest) {
        return new PageQuery(1, 1, 10, null);
    }

    @Override
    public Boolean roleModify(ModifyRoleRequest modifyRoleRequest, String token) {
        return true;
    }

    @Override
    public List<Map<String, Object>> getRoleListByUserused() {
        return new ArrayList<>();
    }

    @Override
    public void userModify(ModifyUserRequest modifyUserRequest, String token) {

    }

    @Override
    public RoleInfoVO getSysRoleInfo(String roleID) {
        return new RoleInfoVO();
    }

    @Override
    public UserInfoVO getUserInfo(String userID) {
        return new UserInfoVO();
    }

    @Override
    public void resetPwd(String userID) {

    }

    @Override
    public Map<String, Object> userLogin(UserLogin userLogin) {
        return null;
    }

    @Override
    public LoginToken getLoginToken(String userID, String getCurrentDateTime) {
        return new LoginToken();
    }

    @Override
    public void updateLoginTokenExpiresTime(String token, String convertDateToString) {

    }

    @Override
    public List<PageInfo> getPageInfoList() {
        return null;
    }

    @Override
    public void insertOperateLog(OperationLog operationLog) {

    }

    @Override
    public void modifyUserPwd(LoginUserPwd loginUserPwd) {

    }

    @Override
    public UserInfoVO getUserInfoVO(String userID, String loginName) {
        return new UserInfoVO();
    }

    @Override
    public Integer getUrlPermissionUrl(String roleID, String uri) {
        return 1;
    }
}
