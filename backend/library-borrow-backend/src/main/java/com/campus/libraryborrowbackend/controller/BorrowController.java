package com.campus.libraryborrowbackend.controller;

import com.campus.libraryborrowbackend.common.Result;
import com.campus.libraryborrowbackend.entity.Book;
import com.campus.libraryborrowbackend.entity.BorrowRecord;
import com.campus.libraryborrowbackend.entity.BorrowRecordExample;
import com.campus.libraryborrowbackend.entity.SysUser;
import com.campus.libraryborrowbackend.mapper.BookMapper;
import com.campus.libraryborrowbackend.mapper.BorrowRecordMapper;
import com.campus.libraryborrowbackend.mapper.SysUserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/borrow")
public class BorrowController {

    @Autowired
    private BorrowRecordMapper borrowRecordMapper;

    @Autowired
    private BookMapper bookMapper;

    @Autowired
    private SysUserMapper sysUserMapper;

    private static final int BORROW_DAYS = 15;
    private static final BigDecimal FINE_PER_DAY = new BigDecimal("0.5");

    /**
     * 图书借阅
     */
    @PostMapping("/add")
    public Result<String> borrowBook(@RequestParam Long userId,
                                     @RequestParam String bookBarcode) {
        SysUser user = sysUserMapper.selectByPrimaryKey(userId);
        if (user == null) {
            return Result.error("用户不存在");
        }

        Book book = bookMapper.selectBookByBarcode(bookBarcode);
        if (book == null) {
            return Result.error("未查询到该图书");
        }
        if (book.getRemainStock() <= 0) {
            return Result.error("图书库存不足，无法借阅");
        }

        book.setRemainStock(book.getRemainStock() - 1);
        bookMapper.updateByPrimaryKey(book);

        BorrowRecord record = new BorrowRecord();
        record.setUserId(userId.intValue());
        record.setBookId(book.getBookId());
        record.setBorrowTime(new Date());
        record.setReturnTime(null);
        record.setIsOverdue((byte) 0);
        record.setFineMoney(new BigDecimal("0.00"));
        borrowRecordMapper.insert(record);

        return Result.success("借阅成功");
    }

    /**
     * 图书归还 + 逾期罚金计算
     */
    @PutMapping("/return")
    public Result<String> returnBook(@RequestParam Integer recordId) {
        BorrowRecord record = borrowRecordMapper.selectByPrimaryKey(recordId);
        if (record == null) {
            return Result.error("借阅记录不存在");
        }
        if (record.getReturnTime() != null) {
            return Result.error("该图书已归还");
        }

        Date borrowTime = record.getBorrowTime();
        Date now = new Date();
        long dayCount = (now.getTime() - borrowTime.getTime()) / (1000 * 60 * 60 * 24);

        byte overdue = 0;
        BigDecimal fine = new BigDecimal("0.00");
        if (dayCount > BORROW_DAYS) {
            overdue = 1;
            long overdueDays = dayCount - BORROW_DAYS;
            fine = FINE_PER_DAY.multiply(new BigDecimal(overdueDays));
        }

        record.setReturnTime(now);
        record.setIsOverdue(overdue);
        record.setFineMoney(fine);
        borrowRecordMapper.updateByPrimaryKey(record);

        Book book = bookMapper.selectByPrimaryKey(record.getBookId());
        book.setRemainStock(book.getRemainStock() + 1);
        bookMapper.updateByPrimaryKey(book);

        return Result.success("归还成功，逾期罚金：" + fine + "元");
    }

    /**
     * 查询用户借阅记录
     */
    @GetMapping("/list")
    public Result<List<BorrowRecord>> listRecords(@RequestParam Long userId) {
        BorrowRecordExample example = new BorrowRecordExample();
        example.createCriteria().andUserIdEqualTo(userId.intValue());
        List<BorrowRecord> list = borrowRecordMapper.selectByExample(example);
        return Result.success(list);
    }
}