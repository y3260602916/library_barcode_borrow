package com.campus.libraryborrowbackend.service;

import com.campus.libraryborrowbackend.entity.LoginDTO;
import com.campus.libraryborrowbackend.entity.SysUser;

import java.util.List;

public interface UserService {
    /**
     * 用户登录校验
     */
    SysUser login(LoginDTO loginDTO);

    /**
     * 获取所有用户列表
     */
    List<SysUser> getAllUsers();

    /**
     * 根据用户类型获取用户列表
     */
    List<SysUser> getUsersByType(Integer userType);

    /**
     * 搜索用户（根据账号或姓名）
     */
    List<SysUser> searchUsers(String keyword);

    /**
     * 添加用户
     */
    void addUser(SysUser user);

    /**
     * 更新用户信息
     */
    void updateUser(SysUser user);

    /**
     * 删除用户
     */
    void deleteUser(Long userId);

    /**
     * 重置密码
     */
    void resetPassword(Long userId, String newPassword);

    /**
     * 更新用户状态
     */
    void updateUserStatus(Long userId, Integer status);
}