package com.campus.libraryborrowbackend.service.impl;

import com.campus.libraryborrowbackend.entity.Book;
import com.campus.libraryborrowbackend.entity.BorrowRecord;
import com.campus.libraryborrowbackend.entity.BorrowRecordExample;
import com.campus.libraryborrowbackend.entity.SysUser;
import com.campus.libraryborrowbackend.mapper.BookMapper;
import com.campus.libraryborrowbackend.mapper.BorrowRecordMapper;
import com.campus.libraryborrowbackend.mapper.SysUserMapper;
import com.campus.libraryborrowbackend.service.BorrowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Service
public class BorrowServiceImpl implements BorrowService {

    @Autowired
    private BorrowRecordMapper borrowRecordMapper;

    @Autowired
    private BookMapper bookMapper;

    @Autowired
    private SysUserMapper sysUserMapper;

    // 常量统一放实现类
    private static final int BORROW_DAYS = 15;
    private static final BigDecimal FINE_PER_DAY = new BigDecimal("0.5");

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String borrowBook(Long userId, String bookBarcode) {
        // 校验用户
        SysUser user = sysUserMapper.selectByPrimaryKey(userId);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }
        // 校验图书
        Book book = bookMapper.selectBookByBarcode(bookBarcode);
        if (book == null) {
            throw new RuntimeException("未查询到该图书");
        }
        if (book.getRemainStock() <= 0) {
            throw new RuntimeException("图书库存不足，无法借阅");
        }
        // 扣库存
        book.setRemainStock(book.getRemainStock() - 1);
        bookMapper.updateByPrimaryKey(book);
        // 新增借阅记录
        BorrowRecord record = new BorrowRecord();
        record.setUserId(userId.intValue());
        record.setBookId(book.getBookId());
        record.setBorrowTime(new Date());
        record.setReturnTime(null);
        record.setIsOverdue((byte) 0);
        record.setFineMoney(new BigDecimal("0.00"));
        borrowRecordMapper.insert(record);

        return "借阅成功";
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String returnBook(Integer recordId) {
        BorrowRecord record = borrowRecordMapper.selectByPrimaryKey(recordId);
        if (record == null) {
            throw new RuntimeException("借阅记录不存在");
        }
        if (record.getReturnTime() != null) {
            throw new RuntimeException("该图书已归还");
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

        // 更新借阅记录
        record.setReturnTime(now);
        record.setIsOverdue(overdue);
        record.setFineMoney(fine);
        borrowRecordMapper.updateByPrimaryKey(record);

        // 回补库存
        Book book = bookMapper.selectByPrimaryKey(record.getBookId());
        book.setRemainStock(book.getRemainStock() + 1);
        bookMapper.updateByPrimaryKey(book);

        return "归还成功，逾期罚金：" + fine + "元";
    }

    @Override
    public List<BorrowRecord> listRecords(Long userId) {
        BorrowRecordExample example = new BorrowRecordExample();
        example.createCriteria().andUserIdEqualTo(userId.intValue());
        return borrowRecordMapper.selectByExample(example);
    }
}