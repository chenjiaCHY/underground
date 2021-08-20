package com.ntschy.underground.service.impl;

import com.ntschy.underground.dao.AuthorityDao;
import com.ntschy.underground.entity.RoleActionMapping;
import com.ntschy.underground.entity.base.LoginUserPwd;
import com.ntschy.underground.entity.base.OperationLog;
import com.ntschy.underground.entity.base.PageQuery;
import com.ntschy.underground.entity.base.Result;
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
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
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
    public Result roleModify(ModifyRoleRequest modifyRoleRequest) {

        String roleId = Optional.ofNullable(modifyRoleRequest.getRoleId()).orElse("");
        String roleName = Optional.ofNullable(modifyRoleRequest.getRoleName()).orElse("");
        Integer operateType = modifyRoleRequest.getOperateType();

        if (StringUtils.isBlank(roleId)) {
            if (operateType > 1) {
                throw new RuntimeException("角色ID不能为空");
            }
            roleId = Utils.GenerateUUID(32);
        }

        if (operateType < 3) {
            if (StringUtils.isBlank(roleName)) {
                throw new RuntimeException("角色名称不能为空");
            }

            Integer count = authorityDao.getRoleCountByName(roleId, roleName);
            if (count > 0) {
                throw new RuntimeException("角色名称已存在，请勿重新创建！！！");
            }
        }

        // 如果是修改和删除，则先从数据库删除mapping关系
        if (operateType > 1) {
            authorityDao.deleteRoleActionMapping(roleId);
        }

        // 如果是删除，则删除一条角色，并结束
        if (operateType == 3) {
            authorityDao.deleteRole(roleId);
            return new Result(true);
        }

        List<String> actionList = Optional.ofNullable(modifyRoleRequest.getActionList()).orElse(Collections.emptyList());
        List<RoleActionMapping> roleActionMappings = new ArrayList<>();

        if (!CollectionUtils.isEmpty(actionList)) {
            for (String actionId : actionList) {
                RoleActionMapping roleActionMapping = new RoleActionMapping();
                roleActionMapping.setRoleId(roleId);
                roleActionMapping.setActionId(actionId);
                roleActionMappings.add(roleActionMapping);
            }
        }

        // 如果是新增和修改，需要插入新的mapping关系
        if (!CollectionUtils.isEmpty(roleActionMappings)) {
            authorityDao.insertRoleActionMapping(roleActionMappings);
        }

        RoleInfoVO roleInfoVO = new RoleInfoVO();
        roleInfoVO.setRoleId(roleId);
        roleInfoVO.setRoleName(modifyRoleRequest.getRoleName());

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        // 如果是新增，则新增一条角色
        if (operateType == 1) {
            roleInfoVO.setCreateTime(sdf.format(new Date()));
            authorityDao.insertRole(roleInfoVO);
        }

        // 如果是更新，则更新一条角色
        if (operateType == 2) {
            authorityDao.updateRole(roleInfoVO);
        }

        return new Result(true);
    }

    @Override
    public Result userModify(ModifyUserRequest modifyUserRequest) {

        String userId = Optional.ofNullable(modifyUserRequest.getUserId()).orElse("");
        Integer operateType = modifyUserRequest.getOperateType();
        String account = Optional.ofNullable(modifyUserRequest.getAccount()).orElse("");

        if (StringUtils.isBlank(userId)) {
            if (operateType > 1) {
                throw new RuntimeException("用户ID不能为空");
            }
            userId = Utils.GenerateUUID(32);
        }

        if (operateType < 3) {
            if (StringUtils.isBlank(account)) {
                throw new RuntimeException("用户名不能为空");
            }

            Integer count = authorityDao.getUserCountByAccount(userId, account);
            if (count > 0) {
                throw new RuntimeException("角色名称已存在，请勿重新创建！！！");
            }
        }

        if (operateType == 1) {
            String password = Optional.ofNullable(modifyUserRequest.getPassword()).orElse("");
            if (StringUtils.isBlank(password)) {
                throw new RuntimeException("密码不能为空");
            }
            modifyUserRequest.setUserId(userId);
            modifyUserRequest.setPassword(MD5Utils.StringToMD5(password));

            authorityDao.insertUser(modifyUserRequest);
        }

        if (operateType == 2) {
            authorityDao.updateUser(modifyUserRequest);
        }

        if (operateType == 3) {
            authorityDao.deleteUser(userId);
        }

        return new Result(true);
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
    public List<Map<String, Object>> getRoleListByUserused() {
        return new ArrayList<>();
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
