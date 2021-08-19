package com.ntschy.underground.service.impl;

import com.ntschy.underground.dao.AuthorityDao;
import com.ntschy.underground.entity.base.LoginUserPwd;
import com.ntschy.underground.entity.base.OperationLog;
import com.ntschy.underground.entity.base.PageInfo;
import com.ntschy.underground.entity.base.PageQuery;
import com.ntschy.underground.entity.dto.*;
import com.ntschy.underground.entity.vo.LoginToken;
import com.ntschy.underground.entity.vo.RoleInfoVO;
import com.ntschy.underground.entity.vo.UserInfoVO;
import com.ntschy.underground.service.AuthorityService;
import com.ntschy.underground.utils.JwtUtil;
import com.ntschy.underground.utils.MD5Utils;
import com.ntschy.underground.utils.Utils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.*;

@Service
public class AuthorityServiceImpl implements AuthorityService {

    @Resource
    private AuthorityDao authorityDao;

    @Override
    public Map<String, Object> userLogin(UserLogin userLogin) {

        UserInfoVO userInfoVO = authorityDao.getSysUserInfo(null, userLogin.getAccount(), MD5Utils.StringToMD5(userLogin.getPwd()), 1);

        if (ObjectUtils.isEmpty(userInfoVO)) {
            throw new RuntimeException("账号或密码错误！！！");
        }

        String roleId = Optional.ofNullable(userInfoVO.getRoleId()).orElse("");
        RoleInfoVO roleInfoVO = null;
        if (StringUtils.isNotBlank(roleId)) {
            roleInfoVO = authorityDao.getSysRoleInfo(roleId);
        }

        String token = JwtUtil.sign(userInfoVO.getAccount(), userInfoVO.getUserId());

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR_OF_DAY, JwtUtil.EXPIRE_HOUR);
        LoginToken loginToken = new LoginToken(userInfoVO.getUserId(), token, Utils.ConvertDateToString(calendar, Utils.YYYYMMDDHH24MMSS), 1, Utils.GetCurrentDateTime());
        loginToken.setTokenId(Utils.GenerateUUID(32));
        authorityDao.insertLoginToken(loginToken);

        Map<String, Object> map = new HashMap<>();
        map.put("token", token);
        map.put("userId", userInfoVO.getUserId());
        map.put("userName", userInfoVO.getName());
        map.put("role", roleInfoVO);

        return map;
    }

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
    public UserInfoVO getUserInfo(String userId) {

        return null;
    }

    @Override
    public void resetPwd(String userID) {

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
