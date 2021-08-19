package com.ntschy.underground.service;

import com.ntschy.underground.entity.base.LoginUserPwd;
import com.ntschy.underground.entity.base.OperationLog;
import com.ntschy.underground.entity.base.PageInfo;
import com.ntschy.underground.entity.base.PageQuery;
import com.ntschy.underground.entity.dto.*;
import com.ntschy.underground.entity.vo.LoginToken;
import com.ntschy.underground.entity.vo.RoleInfoVO;
import com.ntschy.underground.entity.vo.UserInfoVO;

import java.util.List;
import java.util.Map;

public interface AuthorityService {
    PageQuery getRoleList(QueryRoleRequest queryRoleRequest);

    PageQuery getUserList(QueryUserRequest queryUserRequest);

    Boolean roleModify(ModifyRoleRequest modifyRoleRequest, String token);

    List<Map<String, Object>> getRoleListByUserused();

    void userModify(ModifyUserRequest modifyUserRequest, String token);

    RoleInfoVO getSysRoleInfo(String roleID);

    UserInfoVO getUserInfo(String userID);

    void resetPwd(String userID);

    Map<String, Object> userLogin(UserLogin userLogin);

    LoginToken getLoginToken(String userID, String getCurrentDateTime);

    void updateLoginTokenExpiresTime(String token, String convertDateToString);

    List<PageInfo> getPageInfoList();

    void insertOperateLog(OperationLog operationLog);

    void modifyUserPwd(LoginUserPwd loginUserPwd);

    UserInfoVO getUserInfoVO(String userID, String loginName);

    Integer getUrlPermissionUrl(String roleID, String uri);
}
