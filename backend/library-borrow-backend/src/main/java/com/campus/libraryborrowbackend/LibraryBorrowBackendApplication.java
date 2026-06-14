package com.campus.libraryborrowbackend;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@MapperScan("com.campus.libraryborrowbackend.mapper")
public class LibraryBorrowBackendApplication {

    // 注入HTTP请求工具，用于调用Python AI接口
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    public static void main(String[] args) {
        SpringApplication.run(LibraryBorrowBackendApplication.class, args);
    }
}