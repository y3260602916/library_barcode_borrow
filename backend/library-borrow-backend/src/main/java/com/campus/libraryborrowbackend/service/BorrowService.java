package com.campus.libraryborrowbackend.service;

import com.campus.libraryborrowbackend.entity.BorrowRecord;

import java.util.List;

public interface BorrowService {

    /**
     * 图书借阅
     */
    String borrowBook(Long userId, String bookBarcode);

    /**
     * 图书归还
     */
    String returnBook(Integer recordId);

    /**
     * 查询用户借阅记录
     */
    List<BorrowRecord> listRecords(Long userId);
}