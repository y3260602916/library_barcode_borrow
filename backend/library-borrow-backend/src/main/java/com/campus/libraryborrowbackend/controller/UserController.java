package com.campus.libraryborrowbackend.controller;

import com.campus.libraryborrowbackend.common.Result;
import com.campus.libraryborrowbackend.entity.LoginDTO;
import com.campus.libraryborrowbackend.entity.SysUser;
import com.campus.libraryborrowbackend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

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
    public Result<SysUser> login(@RequestBody LoginDTO loginDTO, HttpSession session) {
        try {
            SysUser user = userService.login(loginDTO);
            // 将用户信息存入Session
            session.setAttribute("user", user);
            return Result.success(user);
        } catch (RuntimeException e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 退出登录接口
     * 地址：/user/logout
     */
    @PostMapping("/logout")
    public Result<String> logout(HttpSession session) {
        // 清除Session中的用户信息
        session.removeAttribute("user");
        session.invalidate();
        return Result.success("退出成功");
    }

    /**
     * 获取当前登录用户信息
     * 地址：/user/current
     */
    @GetMapping("/current")
    public Result<SysUser> getCurrentUser(HttpSession session) {
        SysUser user = (SysUser) session.getAttribute("user");
        if (user == null) {
            return Result.error("未登录");
        }
        return Result.success(user);
    }

    // ========== 管理员账号管理接口 ==========

    /**
     * 获取所有用户列表（支持搜索）
     * 地址：/user/list
     */
    @GetMapping("/list")
    public Result<List<SysUser>> getUserList(
            @RequestParam(required = false) Integer userType,
            @RequestParam(required = false) String keyword) {
        List<SysUser> userList;
        if (keyword != null && !keyword.trim().isEmpty()) {
            userList = userService.searchUsers(keyword.trim());
        } else if (userType != null) {
            userList = userService.getUsersByType(userType);
        } else {
            userList = userService.getAllUsers();
        }
        return Result.success(userList);
    }

    /**
     * 添加用户
     * 地址：/user/add
     */
    @PostMapping("/add")
    public Result<String> addUser(@RequestBody SysUser user) {
        try {
            userService.addUser(user);
            return Result.success("添加成功");
        } catch (RuntimeException e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 更新用户信息
     * 地址：/user/update
     */
    @PutMapping("/update")
    public Result<String> updateUser(@RequestBody SysUser user) {
        try {
            userService.updateUser(user);
            return Result.success("更新成功");
        } catch (RuntimeException e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 删除用户
     * 地址：/user/delete/{userId}
     */
    @DeleteMapping("/delete/{userId}")
    public Result<String> deleteUser(@PathVariable Long userId) {
        try {
            userService.deleteUser(userId);
            return Result.success("删除成功");
        } catch (RuntimeException e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 重置密码
     * 地址：/user/reset-password
     */
    @PutMapping("/reset-password")
    public Result<String> resetPassword(@RequestBody Map<String, Object> params) {
        try {
            Long userId = Long.parseLong(params.get("userId").toString());
            String newPassword = params.get("newPassword").toString();
            userService.resetPassword(userId, newPassword);
            return Result.success("密码重置成功");
        } catch (RuntimeException e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 更新用户状态
     * 地址：/user/update-status
     */
    @PutMapping("/update-status")
    public Result<String> updateUserStatus(@RequestBody Map<String, Object> params) {
        try {
            Long userId = Long.parseLong(params.get("userId").toString());
            Integer status = Integer.parseInt(params.get("status").toString());
            userService.updateUserStatus(userId, status);
            return Result.success("状态更新成功");
        } catch (RuntimeException e) {
            return Result.error(e.getMessage());
        }
    }
}