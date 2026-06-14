package com.campus.libraryborrowbackend.service.impl;

import com.campus.libraryborrowbackend.entity.LoginDTO;
import com.campus.libraryborrowbackend.entity.SysUser;
import com.campus.libraryborrowbackend.mapper.BorrowRecordMapper;
import com.campus.libraryborrowbackend.mapper.SysUserMapper;
import com.campus.libraryborrowbackend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private SysUserMapper sysUserMapper;

    @Autowired
    private BorrowRecordMapper borrowRecordMapper;

    @Override
    public SysUser login(LoginDTO loginDTO) {
        // 账号非空校验
        if (loginDTO.getUserAccount() == null || "".equals(loginDTO.getUserAccount().trim())) {
            throw new RuntimeException("账号不能为空");
        }
        // 密码非空校验
        if (loginDTO.getPassword() == null || "".equals(loginDTO.getPassword().trim())) {
            throw new RuntimeException("密码不能为空");
        }

        // 根据账号查询用户
        SysUser user = sysUserMapper.selectByAccount(loginDTO.getUserAccount());
        if (user == null) {
            throw new RuntimeException("账号不存在");
        }

        // 验证密码
        if (!loginDTO.getPassword().equals(user.getPassword())) {
            throw new RuntimeException("密码错误");
        }

        return user;
    }

    @Override
    public List<SysUser> getAllUsers() {
        return sysUserMapper.selectAll();
    }

    @Override
    public List<SysUser> getUsersByType(Integer userType) {
        return sysUserMapper.selectByUserType(userType);
    }

    @Override
    public List<SysUser> searchUsers(String keyword) {
        return sysUserMapper.searchUsers(keyword);
    }

    @Override
    public void addUser(SysUser user) {
        // 检查账号是否已存在
        SysUser existingUser = sysUserMapper.selectByAccount(user.getUserAccount());
        if (existingUser != null) {
            throw new RuntimeException("账号已存在");
        }
        sysUserMapper.insertSelective(user);
    }

    @Override
    public void updateUser(SysUser user) {
        sysUserMapper.updateByPrimaryKeySelective(user);
    }

    @Override
    public void deleteUser(Long userId) {
        // 检查用户是否有未归还的借阅记录
        int borrowCount = borrowRecordMapper.selectUserBorrowCount(userId.intValue());
        if (borrowCount > 0) {
            throw new RuntimeException("删除失败，该用户还有 " + borrowCount + " 本图书未归还，请先归还所有图书");
        }
        
        try {
            // 先删除用户的所有历史借阅记录（已归还的）
            borrowRecordMapper.deleteByUserId(userId.intValue());
            // 再删除用户
            sysUserMapper.deleteByPrimaryKey(userId);
        } catch (Exception e) {
            throw new RuntimeException("删除失败: " + e.getMessage());
        }
    }

    @Override
    public void resetPassword(Long userId, String newPassword) {
        SysUser user = new SysUser();
        user.setUserId(userId.intValue());
        user.setPassword(newPassword);
        sysUserMapper.updateByPrimaryKeySelective(user);
    }

    @Override
    public void updateUserStatus(Long userId, Integer status) {
        // 由于SysUser表没有status字段，此方法暂不实现
        // 需要先在数据库表中添加status字段才能实现此功能
        throw new RuntimeException("用户状态管理功能暂未实现，请先在数据库表中添加status字段");
    }
}