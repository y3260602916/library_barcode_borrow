package com.campus.libraryborrowbackend.controller;

import com.campus.libraryborrowbackend.common.Result;
import com.campus.libraryborrowbackend.entity.Book;
import com.campus.libraryborrowbackend.entity.BookExample;
import com.campus.libraryborrowbackend.mapper.BookMapper;
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
    private BookMapper bookMapper;

    /**
     * 图书列表查询 + 关键词搜索
     * 接口地址：GET /book/list?keyword=xxx
     */
    @GetMapping("/list")
    public Result<List<Book>> getBookList(@RequestParam(required = false) String keyword) {
        BookExample example = new BookExample();
        BookExample.Criteria criteria = example.createCriteria();

        // 关键词非空：按 图书名称 / 作者 模糊搜索
        if (keyword != null && !keyword.trim().isEmpty()) {
            // 新建一个 or 条件组，实现 OR 查询
            BookExample.Criteria orCriteria = example.or();
            orCriteria.andBookNameLike("%" + keyword + "%")
                    .andAuthorLike("%" + keyword + "%");
        }

        List<Book> bookList = bookMapper.selectByExample(example);
        return Result.success(bookList);
    }
}