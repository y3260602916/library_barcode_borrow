package com.campus.libraryborrowbackend.service;

import com.campus.libraryborrowbackend.entity.LoginDTO;
import com.campus.libraryborrowbackend.entity.SysUser;

public interface UserService {
    /**
     * 用户登录校验
     */
    SysUser login(LoginDTO loginDTO);
}