package com.ntschy.underground.dao;

import com.ntschy.underground.datasource.annotation.DataSource;
import com.ntschy.underground.entity.Action;
import com.ntschy.underground.entity.RoleActionMapping;
import com.ntschy.underground.entity.base.OperationLog;
import com.ntschy.underground.entity.dto.ModifyUserRequest;
import com.ntschy.underground.entity.vo.LoginToken;
import com.ntschy.underground.entity.vo.RoleInfoVO;
import com.ntschy.underground.entity.vo.UserInfoVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AuthorityDao {
    @DataSource("slave2")
    RoleInfoVO getRoleInfo(@Param("roleId") String roleID) throws RuntimeException;

    @DataSource("slave2")
    UserInfoVO getUserInfo(@Param("userId") String userId,
                              @Param("account") String account,
                              @Param("pwd") String pwd,
                              @Param("status") Integer status) throws RuntimeException;

    @DataSource("slave2")
    void insertLoginToken(LoginToken loginToken) throws RuntimeException;

    @DataSource("slave2")
    Integer getRoleCountByName(@Param("roleId") String roleId, @Param("roleName") String roleName) throws RuntimeException;

    @DataSource("slave2")
    void deleteRoleActionMapping(@Param("roleId") String roleId) throws RuntimeException;

    @DataSource("slave2")
    void deleteRole(@Param("roleId") String roleId) throws RuntimeException;

    @DataSource("slave2")
    void insertRoleActionMapping(@Param("mappings") List<RoleActionMapping> mappings) throws RuntimeException;

    @DataSource("slave2")
    void insertRole(RoleInfoVO roleInfoVO) throws RuntimeException;

    @DataSource("slave2")
    void updateRole(RoleInfoVO roleInfoVO) throws RuntimeException;

    @DataSource("slave2")
    Integer getUserCountByAccount(@Param("userId") String userId, @Param("account") String account) throws RuntimeException;

    @DataSource("slave2")
    void insertUser(ModifyUserRequest modifyUserRequest) throws RuntimeException;

    @DataSource("slave2")
    void updateUser(ModifyUserRequest modifyUserRequest) throws RuntimeException;

    @DataSource("slave2")
    void deleteUser(@Param("userId") String userId) throws RuntimeException;

    @DataSource("slave2")
    List<Action> getActionList() throws RuntimeException;

    @DataSource("slave2")
    List<RoleInfoVO> getFullRoleList() throws RuntimeException;

    @DataSource("slave2")
    Integer getRoleCount() throws RuntimeException;

    @DataSource("slave2")
    List<RoleInfoVO> getRoleList(@Param("startNo") Integer startNo,
                                 @Param("endNo") Integer endNo) throws RuntimeException;

    @DataSource("slave2")
    List<Action> getActionListByRoleId(@Param("roleId") String roleId) throws RuntimeException;

    @DataSource("slave2")
    Integer getRoleInUseCount(@Param("roleId") String roleId) throws RuntimeException;

    @DataSource("slave2")
    Integer getUserCount(@Param("account") String account) throws RuntimeException;

    @DataSource("slave2")
    List<UserInfoVO> getUserList(@Param("account") String account,
                                 @Param("startNo") Integer startNo,
                                 @Param("endNo") Integer endNo) throws RuntimeException;

    @DataSource("slave2")
    void modifyUserPwd(@Param("account") String account,
                       @Param("newPwd") String newPwd) throws RuntimeException;

    @DataSource("slave2")
    void activeUser(@Param("userIdList") List<String> userIdList,
                    @Param("status") Integer status) throws RuntimeException;

    @DataSource("slave2")
    LoginToken getLoginToken(@Param("userId") String userId, @Param("expiresTime") String expiresTime) throws RuntimeException;

    @DataSource("slave2")
    Integer getUrlPermissionUrl(@Param("roleId") String roleId, @Param("uri") String uri) throws RuntimeException;

    @DataSource("slave2")
    void updateLoginTokenExpiresTime(@Param("token") String token, @Param("expiresTime") String expiresTime) throws RuntimeException;

    @DataSource("slave2")
    void insertOperateLog(OperationLog operationLog) throws RuntimeException;
}
