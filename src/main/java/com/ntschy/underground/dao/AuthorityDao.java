package com.ntschy.underground.dao;

import com.ntschy.underground.datasource.annotation.DataSource;
import com.ntschy.underground.entity.vo.RoleInfoVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface AuthorityDao {
    @DataSource("slave1")
    RoleInfoVO getSysRoleInfo(@Param("roleID") String roleID);

}
