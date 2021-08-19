package com.ntschy.underground.dao;

import com.ntschy.underground.datasource.annotation.DataSource;
import com.ntschy.underground.entity.vo.RoleInfoVO;
import com.ntschy.underground.entity.vo.UserInfoVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface AuthorityDao {
    @DataSource("slave1")
    RoleInfoVO getSysRoleInfo(@Param("roleID") String roleID);

    @DataSource("slave1")
    UserInfoVO getSysUserInfo(@Param("userId") String userId);

}
