package com.ntschy.underground.service.impl;

import com.ntschy.underground.dao.AuthorityDao;
import com.ntschy.underground.entity.Action;
import com.ntschy.underground.entity.RoleActionMapping;
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

    /**
     * 用户登录
     * @param userLogin
     * @return
     */
    @Override
    public Map<String, Object> userLogin(UserLogin userLogin) {

        UserInfoVO userInfoVO = authorityDao.getUserInfo(null, userLogin.getAccount(), MD5Utils.StringToMD5(userLogin.getPwd()), 1);

        if (ObjectUtils.isEmpty(userInfoVO)) {
            throw new RuntimeException("账号或密码错误！！！");
        }

        String roleId = Optional.ofNullable(userInfoVO.getRoleId()).orElse("");
        RoleInfoVO roleInfoVO = null;
        if (StringUtils.isNotBlank(roleId)) {
            roleInfoVO = authorityDao.getRoleInfo(roleId);
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

    /**
     * 角色新增、修改、删除
     * @param modifyRoleRequest
     * @return
     */
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

    /**
     * 用户新增、修改、删除
     * @param modifyUserRequest
     * @return
     */
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
                throw new RuntimeException("用户名已存在，请勿重新创建！！！");
            }
        }

        if (operateType == 1) {
            String password = Optional.ofNullable(modifyUserRequest.getPassword()).orElse("");
            if (StringUtils.isBlank(password)) {
                throw new RuntimeException("密码不能为空");
            }
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            modifyUserRequest.setUserId(userId);
            modifyUserRequest.setPassword(MD5Utils.StringToMD5(password));
            modifyUserRequest.setCreateTime(sdf.format(new Date()));

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

    /**
     * 获取所有权限列表
     * @return
     */
    @Override
    public Result getActionList() {

        List<Action> actionList = authorityDao.getActionList();

        actionList = Optional.ofNullable(actionList).orElse(Collections.emptyList());

        return new Result<>(actionList);
    }

    /**
     * 获取所有角色列表
     * @return
     */
    @Override
    public Result getFullRoleList() {

        List<RoleInfoVO> roleList = authorityDao.getFullRoleList();

        roleList = Optional.ofNullable(roleList).orElse(Collections.emptyList());

        return new Result<>(roleList);
    }

    /**
     * 获取角色列表
     * @param queryRoleRequest
     * @return
     */
    @Override
    public PageQuery getRoleList(QueryRoleRequest queryRoleRequest) {
        Integer startNo = PageQuery.startLine(queryRoleRequest.getCurrPage(), queryRoleRequest.getPageSize());
        Integer endNo = PageQuery.endLine(queryRoleRequest.getCurrPage(), queryRoleRequest.getPageSize());

        Integer total = authorityDao.getRoleCount();

        List<RoleInfoVO> roleInfoVOList = authorityDao.getRoleList(startNo, endNo);

        int rowNumber = (queryRoleRequest.getCurrPage() - 1) * queryRoleRequest.getPageSize() + 1;

        if (!CollectionUtils.isEmpty(roleInfoVOList)) {
            for (RoleInfoVO roleInfo : roleInfoVOList) {
                roleInfo.setRowNumber(rowNumber);
                // 获取该角色有哪些权限
                List<Action> actionList = authorityDao.getActionListByRoleId(roleInfo.getRoleId());
                roleInfo.setActionList(actionList);

                // 获取该角色被多少用户使用
                Integer inUseCount = authorityDao.getRoleInUseCount(roleInfo.getRoleId());
                roleInfo.setInUseCount(inUseCount);

                rowNumber ++;
            }
        }

        PageQuery pageQuery = new PageQuery(queryRoleRequest.getCurrPage(), queryRoleRequest.getPageSize(),
                total, Optional.ofNullable(roleInfoVOList).orElse(Collections.emptyList()));

        return pageQuery;
    }

    /**
     * 获取用户列表
     * @param queryUserRequest
     * @return
     */
    @Override
    public PageQuery getUserList(QueryUserRequest queryUserRequest) {
        Integer startNo = PageQuery.startLine(queryUserRequest.getCurrPage(), queryUserRequest.getPageSize());
        Integer endNo = PageQuery.endLine(queryUserRequest.getCurrPage(), queryUserRequest.getPageSize());

        Integer total = authorityDao.getUserCount(queryUserRequest.getAccount());

        List<UserInfoVO> userInfoList = authorityDao.getUserList(queryUserRequest.getAccount(), startNo, endNo);

        int rowNumber = (queryUserRequest.getCurrPage() - 1) * queryUserRequest.getPageSize() + 1;

        if (!CollectionUtils.isEmpty(userInfoList)) {
            for (UserInfoVO userInfo : userInfoList) {
                userInfo.setRowNumber(rowNumber);
                rowNumber ++;
            }
        }

        PageQuery pageQuery = new PageQuery(queryUserRequest.getCurrPage(), queryUserRequest.getPageSize(),
                total, Optional.ofNullable(userInfoList).orElse(Collections.emptyList()));

        return pageQuery;
    }

    @Override
    public RoleInfoVO getRoleInfo(String roleId) {

        RoleInfoVO roleInfoVO = authorityDao.getRoleInfo(roleId);

        return roleInfoVO;

    }

    @Override
    public UserInfoVO getUserInfo(String userId) {

        UserInfoVO userInfoVO = authorityDao.getUserInfo(userId, null, null, null);

        return userInfoVO;
    }

    @Override
    public Result activeUser(ActiveUserRequest activeUserRequest) {

        authorityDao.activeUser(activeUserRequest.getUserIdList(), activeUserRequest.getStatus());

        String message = "启用成功";

        if (activeUserRequest.getStatus() == 0) {
            message = "禁用成功";
        }

        return new Result(true, message);
    }

    @Override
    public LoginToken getLoginToken(String userID, String getCurrentDateTime) {
        return authorityDao.getLoginToken(userID, getCurrentDateTime);
    }

    @Override
    public void updateLoginTokenExpiresTime(String token, String convertDateToString) {
        authorityDao.updateLoginTokenExpiresTime(token, convertDateToString);
    }

    @Override
    public void insertOperateLog(OperationLog operationLog) {

    }

    @Override
    public Result modifyUserPwd(ModifyPwdRequest modifyPwdRequest) {

        modifyPwdRequest.setNewPwd(MD5Utils.StringToMD5(modifyPwdRequest.getNewPwd()));
        authorityDao.modifyUserPwd(modifyPwdRequest.getAccount(), modifyPwdRequest.getNewPwd());

        return new Result(true, "更新密码成功！");

    }

    @Override
    public Integer getUrlPermissionUrl(String roleId, String uri) {
        return authorityDao.getUrlPermissionUrl(roleId, uri);
    }
}
