package com.campus.libraryborrowbackend.entity;

import lombok.Data;

@Data
public class LoginDTO {
    // 登录账号
    private String userAccount;
    // 密码（简易版：数据库暂时无密码字段，先做模拟校验）
    private String password;
}