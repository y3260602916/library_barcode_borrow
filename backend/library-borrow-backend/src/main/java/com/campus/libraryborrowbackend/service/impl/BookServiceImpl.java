package com.campus.libraryborrowbackend.service.impl;

import com.campus.libraryborrowbackend.entity.Book;
import com.campus.libraryborrowbackend.entity.BookExample;
import com.campus.libraryborrowbackend.mapper.BookMapper;
import com.campus.libraryborrowbackend.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class BookServiceImpl implements BookService {

    @Autowired
    private BookMapper bookMapper;

    @Override
    public List<Book> getBookList(String keyword) {
        BookExample example = new BookExample();
        BookExample.Criteria criteria = example.createCriteria();

        if (keyword != null && !keyword.trim().isEmpty()) {
            BookExample.Criteria orCriteria = example.or();
            orCriteria.andBookNameLike("%" + keyword + "%")
                    .andAuthorLike("%" + keyword + "%");
        }
        return bookMapper.selectByExample(example);
    }
}