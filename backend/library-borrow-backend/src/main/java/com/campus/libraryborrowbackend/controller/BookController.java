package com.campus.libraryborrowbackend.controller;

import com.campus.libraryborrowbackend.common.Result;
import com.campus.libraryborrowbackend.entity.Book;
import com.campus.libraryborrowbackend.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/book")
public class BookController {

    @Autowired
    private BookService bookService;

    /**
     * 图书列表查询 + 关键词搜索
     * 接口地址：GET /book/list?keyword=xxx
     */
    @GetMapping("/list")
    public Result<List<Book>> getBookList(@RequestParam(required = false) String keyword) {
        List<Book> bookList = bookService.getBookList(keyword);
        return Result.success(bookList);
    }
}