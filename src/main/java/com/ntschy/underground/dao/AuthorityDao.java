package com.ntschy.underground.dao;

import com.ntschy.underground.datasource.annotation.DataSource;
import com.ntschy.underground.entity.Action;
import com.ntschy.underground.entity.RoleActionMapping;
import com.ntschy.underground.entity.dto.ModifyUserRequest;
import com.ntschy.underground.entity.vo.LoginToken;
import com.ntschy.underground.entity.vo.RoleInfoVO;
import com.ntschy.underground.entity.vo.UserInfoVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AuthorityDao {
    @DataSource("slave1")
    RoleInfoVO getRoleInfo(@Param("roleId") String roleID) throws RuntimeException;

    @DataSource("slave1")
    UserInfoVO getUserInfo(@Param("userId") String userId,
                              @Param("account") String account,
                              @Param("pwd") String pwd,
                              @Param("status") Integer status) throws RuntimeException;

    @DataSource("slave1")
    void insertLoginToken(LoginToken loginToken) throws RuntimeException;

    @DataSource("slave1")
    Integer getRoleCountByName(@Param("roleId") String roleId, @Param("roleName") String roleName) throws RuntimeException;

    @DataSource("slave1")
    void deleteRoleActionMapping(@Param("roleId") String roleId) throws RuntimeException;

    @DataSource("slave1")
    void deleteRole(@Param("roleId") String roleId) throws RuntimeException;

    @DataSource("slave1")
    void insertRoleActionMapping(@Param("mappings") List<RoleActionMapping> mappings) throws RuntimeException;

    @DataSource("slave1")
    void insertRole(RoleInfoVO roleInfoVO) throws RuntimeException;

    @DataSource("slave1")
    void updateRole(RoleInfoVO roleInfoVO) throws RuntimeException;

    @DataSource("slave1")
    Integer getUserCountByAccount(@Param("userId") String userId, @Param("account") String account) throws RuntimeException;

    @DataSource("slave1")
    void insertUser(ModifyUserRequest modifyUserRequest) throws RuntimeException;

    @DataSource("slave1")
    void updateUser(ModifyUserRequest modifyUserRequest) throws RuntimeException;

    @DataSource("slave1")
    void deleteUser(@Param("userId") String userId) throws RuntimeException;

    @DataSource("slave1")
    List<Action> getActionList() throws RuntimeException;

    @DataSource("slave1")
    List<RoleInfoVO> getFullRoleList() throws RuntimeException;

    @DataSource("slave1")
    Integer getRoleCount() throws RuntimeException;

    @DataSource("slave1")
    List<RoleInfoVO> getRoleList(@Param("startNo") Integer startNo,
                                 @Param("endNo") Integer endNo) throws RuntimeException;

    @DataSource("slave1")
    List<Action> getActionListByRoleId(@Param("roleId") String roleId) throws RuntimeException;

    @DataSource("slave1")
    Integer getRoleInUseCount(@Param("roleId") String roleId) throws RuntimeException;

    @DataSource("slave1")
    Integer getUserCount(@Param("account") String account) throws RuntimeException;

    @DataSource("slave1")
    List<UserInfoVO> getUserList(@Param("account") String account,
                                 @Param("startNo") Integer startNo,
                                 @Param("endNo") Integer endNo) throws RuntimeException;

    @DataSource("slave1")
    void modifyUserPwd(@Param("account") String account,
                       @Param("newPwd") String newPwd) throws RuntimeException;

    @DataSource("slave1")
    void activeUser(@Param("userIdList") List<String> userIdList,
                    @Param("status") Integer status) throws RuntimeException;
}
