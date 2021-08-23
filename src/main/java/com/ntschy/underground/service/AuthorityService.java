package com.ntschy.underground.service;

import com.ntschy.underground.entity.base.OperationLog;
import com.ntschy.underground.entity.base.PageQuery;
import com.ntschy.underground.entity.base.Result;
import com.ntschy.underground.entity.dto.*;
import com.ntschy.underground.entity.vo.LoginToken;
import com.ntschy.underground.entity.vo.RoleInfoVO;
import com.ntschy.underground.entity.vo.UserInfoVO;

import java.util.Map;

public interface AuthorityService {

    Map<String, Object> userLogin(UserLogin userLogin);

    Result roleModify(ModifyRoleRequest modifyRoleRequest);

    Result userModify(ModifyUserRequest modifyUserRequest);

    Result getActionList();

    Result getFullRoleList();

    PageQuery getRoleList(QueryRoleRequest queryRoleRequest);

    PageQuery getUserList(QueryUserRequest queryUserRequest);

    RoleInfoVO getRoleInfo(String roleId);

    UserInfoVO getUserInfo(String userId);

    LoginToken getLoginToken(String userID, String getCurrentDateTime);

    void updateLoginTokenExpiresTime(String token, String convertDateToString);

    void insertOperateLog(OperationLog operationLog);

    Result modifyUserPwd(ModifyPwdRequest modifyPwdRequest);

    UserInfoVO getUserInfoVO(String userID, String loginName);

    Integer getUrlPermissionUrl(String roleID, String uri);

    Result activeUser(ActiveUserRequest activeUserRequest);
}
