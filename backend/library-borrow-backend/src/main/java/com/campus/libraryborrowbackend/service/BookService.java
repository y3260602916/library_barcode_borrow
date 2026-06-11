package com.campus.libraryborrowbackend.service;

import com.campus.libraryborrowbackend.entity.Book;
import java.util.List;

public interface BookService {

    /**
     * 分页/条件查询图书列表
     * @param keyword 搜索关键词
     * @return 图书集合
     */
    List<Book> getBookList(String keyword);
}