package com.campus.libraryborrowbackend.service.impl;

import com.campus.libraryborrowbackend.entity.LoginDTO;
import com.campus.libraryborrowbackend.entity.SysUser;
import com.campus.libraryborrowbackend.mapper.SysUserMapper;
import com.campus.libraryborrowbackend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private SysUserMapper sysUserMapper;

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

        // 后续加密码字段后，在此处补充密码比对逻辑
        return user;
    }
}