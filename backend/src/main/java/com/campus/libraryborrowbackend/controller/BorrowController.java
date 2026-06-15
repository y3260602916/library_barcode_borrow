package com.campus.libraryborrowbackend.controller;

import com.campus.libraryborrowbackend.common.Result;
import com.campus.libraryborrowbackend.entity.BorrowRecord;
import com.campus.libraryborrowbackend.service.BorrowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

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
     * 查询用户借阅记录（包含图书名称）
     */
    @GetMapping("/list")
    public Result<List<Map<String, Object>>> listRecords(@RequestParam Long userId) {
        List<Map<String, Object>> list = ((com.campus.libraryborrowbackend.service.impl.BorrowServiceImpl) borrowService).listRecordsWithBookName(userId);
        return Result.success(list);
    }

    /**
     * 查询用户在借图书（未归还的）
     */
    @GetMapping("/borrowing")
    public Result<List<Map<String, Object>>> getBorrowingBooks(@RequestParam Long userId) {
        List<Map<String, Object>> list = borrowService.getBorrowingBooks(userId);
        return Result.success(list);
    }

    /**
     * 查询用户逾期提醒
     */
    @GetMapping("/overdue")
    public Result<List<Map<String, Object>>> getOverdueReminder(@RequestParam Long userId) {
        List<Map<String, Object>> list = borrowService.getOverdueReminder(userId);
        return Result.success(list);
    }

    // ========== 管理员逾期处理接口 ==========

    /**
     * 查询全馆逾期记录
     */
    @GetMapping("/overdue/all")
    public Result<List<Map<String, Object>>> getAllOverdueRecords() {
        List<Map<String, Object>> list = borrowService.getAllOverdueRecords();
        return Result.success(list);
    }

    /**
     * 查询全馆未归还借阅记录（包含即将到期和逾期）
     */
    @GetMapping("/not-returned/all")
    public Result<List<Map<String, Object>>> getAllNotReturnedRecords() {
        List<Map<String, Object>> list = borrowService.getAllNotReturnedRecords();
        return Result.success(list);
    }

    /**
     * 批量催还（发送通知）
     */
    @PostMapping("/overdue/remind")
    public Result<String> remindOverdueUsers(@RequestBody List<Integer> recordIds) {
        try {
            String msg = borrowService.remindOverdueUsers(recordIds);
            return Result.success(msg);
        } catch (RuntimeException e) {
            return Result.error(e.getMessage());
        }
    }
}