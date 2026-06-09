package com.campus.libraryborrowbackend.controller;

import com.campus.libraryborrowbackend.common.Result;
import com.campus.libraryborrowbackend.entity.LoginDTO;
import com.campus.libraryborrowbackend.entity.SysUser;
import com.campus.libraryborrowbackend.mapper.SysUserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private SysUserMapper sysUserMapper;

    /**
     * 登录接口
     * 地址：/user/login
     */
    @PostMapping("/login")
    public Result<SysUser> login(@RequestBody LoginDTO loginDTO) {
        // 1. 非空校验
        if (loginDTO.getUserAccount() == null || "".equals(loginDTO.getUserAccount().trim())) {
            return Result.error("账号不能为空");
        }
        if (loginDTO.getPassword() == null || "".equals(loginDTO.getPassword().trim())) {
            return Result.error("密码不能为空");
        }

        // 2. 根据账号查询用户
        SysUser user = sysUserMapper.selectByAccount(loginDTO.getUserAccount());
        if (user == null) {
            return Result.error("账号不存在");
        }

        // 3. 简易密码校验（表无密码字段，仅判断非空）
        // 后续新增密码字段后，替换为 数据库密码比对
        return Result.success(user);
    }
}