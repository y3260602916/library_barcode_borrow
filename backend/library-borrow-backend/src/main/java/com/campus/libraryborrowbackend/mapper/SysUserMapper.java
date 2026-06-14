package com.campus.libraryborrowbackend.mapper;

import com.campus.libraryborrowbackend.entity.SysUser;

import java.util.List;

/**
* @author ASUS
* @description 针对表【sys_user(师生与管理员表)】的数据库操作Mapper
* @createDate 2026-06-09 08:53:48
* @Entity com.campus.libraryborrowbackend.entity.SysUser
*/
public interface SysUserMapper {

    int deleteByPrimaryKey(Long id);

    int insert(SysUser record);

    int insertSelective(SysUser record);

    SysUser selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(SysUser record);

    int updateByPrimaryKey(SysUser record);

    SysUser selectByAccount(String userAccount);

    /**
     * 查询所有用户
     */
    List<SysUser> selectAll();

    /**
     * 根据用户类型查询用户
     */
    List<SysUser> selectByUserType(Integer userType);

    /**
     * 搜索用户（根据账号或姓名）
     */
    List<SysUser> searchUsers(String keyword);
}
