package com.campus.libraryborrowbackend.entity;

import lombok.Data;
import java.util.Date;

@Data
public class User {
    private Integer userId;
    private String userName;
    private String userAccount;
    private Integer userType;
    private Date createTime;
}