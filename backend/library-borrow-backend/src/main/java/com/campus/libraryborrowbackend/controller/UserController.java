package com.campus.libraryborrowbackend.controller;

import com.campus.libraryborrowbackend.common.Result;
import com.campus.libraryborrowbackend.entity.LoginDTO;
import com.campus.libraryborrowbackend.entity.SysUser;
import com.campus.libraryborrowbackend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 登录接口
     * 地址：/user/login
     */
    @PostMapping("/login")
    public Result<SysUser> login(@RequestBody LoginDTO loginDTO) {
        try {
            SysUser user = userService.login(loginDTO);
            return Result.success(user);
        } catch (RuntimeException e) {
            return Result.error(e.getMessage());
        }
    }
}