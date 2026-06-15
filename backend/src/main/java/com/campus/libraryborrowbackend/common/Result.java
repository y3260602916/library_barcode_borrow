package com.campus.libraryborrowbackend.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result<T> {
    private Integer code;   // 状态码 200成功 500失败
    private String msg;     // 提示信息
    private T data;         // 返回数据

    // 成功响应
    public static <T> Result<T> success(T data) {
        return new Result<>(200, "操作成功", data);
    }

    // 失败响应
    public static <T> Result<T> error(String msg) {
        return new Result<>(500, msg, null);
    }
}