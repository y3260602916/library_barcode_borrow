package com.campus.libraryborrowbackend;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
// 加上这一行，扫描Mapper接口
@MapperScan("com.campus.libraryborrowbackend.mapper")
public class LibraryBorrowBackendApplication {
    public static void main(String[] args) {
        SpringApplication.run(LibraryBorrowBackendApplication.class, args);
    }
}