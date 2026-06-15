package com.campus.libraryborrowbackend.service;

import com.campus.libraryborrowbackend.entity.BorrowRecord;

import java.util.List;
import java.util.Map;

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

    /**
     * 查询用户在借图书（未归还的）
     */
    List<Map<String, Object>> getBorrowingBooks(Long userId);

    /**
     * 查询用户逾期提醒
     */
    List<Map<String, Object>> getOverdueReminder(Long userId);

    /**
     * 查询全馆逾期记录
     */
    List<Map<String, Object>> getAllOverdueRecords();

    /**
     * 批量催还（发送通知）
     */
    String remindOverdueUsers(List<Integer> recordIds);
}