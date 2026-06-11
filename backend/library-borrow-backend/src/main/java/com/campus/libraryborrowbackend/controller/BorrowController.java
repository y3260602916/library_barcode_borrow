package com.campus.libraryborrowbackend.controller;

import com.campus.libraryborrowbackend.common.Result;
import com.campus.libraryborrowbackend.entity.BorrowRecord;
import com.campus.libraryborrowbackend.service.BorrowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/borrow")
public class BorrowController {

    // 只注入 Service
    @Autowired
    private BorrowService borrowService;

    /**
     * 图书借阅
     */
    @PostMapping("/add")
    public Result<String> borrowBook(@RequestParam Long userId,
                                     @RequestParam String bookBarcode) {
        try {
            String msg = borrowService.borrowBook(userId, bookBarcode);
            return Result.success(msg);
        } catch (RuntimeException e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 图书归还
     */
    @PutMapping("/return")
    public Result<String> returnBook(@RequestParam Integer recordId) {
        try {
            String msg = borrowService.returnBook(recordId);
            return Result.success(msg);
        } catch (RuntimeException e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 查询用户借阅记录
     */
    @GetMapping("/list")
    public Result<List<BorrowRecord>> listRecords(@RequestParam Long userId) {
        List<BorrowRecord> list = borrowService.listRecords(userId);
        return Result.success(list);
    }
}