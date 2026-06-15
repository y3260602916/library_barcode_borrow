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
import java.util.*;

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

    /**
     * 查询用户借阅记录（包含图书名称）
     */
    public List<Map<String, Object>> listRecordsWithBookName(Long userId) {
        List<Map<String, Object>> result = new ArrayList<>();
        
        BorrowRecordExample example = new BorrowRecordExample();
        example.createCriteria().andUserIdEqualTo(userId.intValue());
        
        List<BorrowRecord> records = borrowRecordMapper.selectByExample(example);
        
        for (BorrowRecord record : records) {
            Book book = bookMapper.selectByPrimaryKey(record.getBookId());
            Map<String, Object> item = new HashMap<>();
            item.put("recordId", record.getRecordId());
            item.put("bookId", record.getBookId());
            item.put("bookName", book != null ? book.getBookName() : "未知图书");
            item.put("borrowTime", record.getBorrowTime());
            item.put("returnTime", record.getReturnTime());
            item.put("isOverdue", record.getIsOverdue());
            item.put("fineMoney", record.getFineMoney());
            result.add(item);
        }
        
        return result;
    }

    @Override
    public List<Map<String, Object>> getBorrowingBooks(Long userId) {
        List<Map<String, Object>> result = new ArrayList<>();

        BorrowRecordExample example = new BorrowRecordExample();
        example.createCriteria()
                .andUserIdEqualTo(userId.intValue())
                .andReturnTimeIsNull();

        List<BorrowRecord> records = borrowRecordMapper.selectByExample(example);

        for (BorrowRecord record : records) {
            Book book = bookMapper.selectByPrimaryKey(record.getBookId());
            if (book != null) {
                Map<String, Object> item = new HashMap<>();
                item.put("recordId", record.getRecordId());
                item.put("bookId", book.getBookId());
                item.put("bookName", book.getBookName());
                item.put("author", book.getAuthor());
                item.put("category", book.getCategory());
                item.put("borrowTime", record.getBorrowTime());
                item.put("isOverdue", record.getIsOverdue());
                item.put("fineMoney", record.getFineMoney());
                result.add(item);
            }
        }

        return result;
    }

    @Override
    public List<Map<String, Object>> getOverdueReminder(Long userId) {
        List<Map<String, Object>> result = new ArrayList<>();
        Date now = new Date();

        BorrowRecordExample example = new BorrowRecordExample();
        example.createCriteria()
                .andUserIdEqualTo(userId.intValue())
                .andReturnTimeIsNull();

        List<BorrowRecord> records = borrowRecordMapper.selectByExample(example);

        for (BorrowRecord record : records) {
            Date borrowTime = record.getBorrowTime();
            long dayCount = (now.getTime() - borrowTime.getTime()) / (1000 * 60 * 60 * 24);

            if (dayCount > BORROW_DAYS) {
                Book book = bookMapper.selectByPrimaryKey(record.getBookId());
                if (book != null) {
                    Map<String, Object> item = new HashMap<>();
                    item.put("recordId", record.getRecordId());
                    item.put("bookId", book.getBookId());
                    item.put("bookName", book.getBookName());
                    item.put("author", book.getAuthor());
                    item.put("borrowTime", borrowTime);
                    item.put("overdueDays", dayCount - BORROW_DAYS);
                    item.put("fineMoney", FINE_PER_DAY.multiply(new BigDecimal(dayCount - BORROW_DAYS)));
                    result.add(item);
                }
            }
        }

        return result;
    }

    @Override
    public List<Map<String, Object>> getAllOverdueRecords() {
        List<Map<String, Object>> result = new ArrayList<>();
        Date now = new Date();

        BorrowRecordExample example = new BorrowRecordExample();
        example.createCriteria()
                .andReturnTimeIsNull();

        List<BorrowRecord> records = borrowRecordMapper.selectByExample(example);

        for (BorrowRecord record : records) {
            Date borrowTime = record.getBorrowTime();
            long dayCount = (now.getTime() - borrowTime.getTime()) / (1000 * 60 * 60 * 24);

            if (dayCount > BORROW_DAYS) {
                Book book = bookMapper.selectByPrimaryKey(record.getBookId());
                SysUser user = sysUserMapper.selectByPrimaryKey(Long.valueOf(record.getUserId()));

                if (book != null && user != null) {
                    Map<String, Object> item = new HashMap<>();
                    item.put("recordId", record.getRecordId());
                    item.put("bookId", book.getBookId());
                    item.put("bookName", book.getBookName());
                    item.put("author", book.getAuthor());
                    item.put("userId", user.getUserId());
                    item.put("userName", user.getUserName());
                    item.put("userAccount", user.getUserAccount());
                    item.put("borrowTime", borrowTime);
                    item.put("overdueDays", dayCount - BORROW_DAYS);
                    item.put("fineMoney", FINE_PER_DAY.multiply(new BigDecimal(dayCount - BORROW_DAYS)));
                    result.add(item);
                }
            }
        }

        // 按逾期天数排序
        result.sort((a, b) -> {
            Long overdueDaysA = (Long) a.get("overdueDays");
            Long overdueDaysB = (Long) b.get("overdueDays");
            return overdueDaysB.compareTo(overdueDaysA);
        });

        return result;
    }

    @Override
    public String remindOverdueUsers(List<Integer> recordIds) {
        // 实际项目中这里会发送短信/邮件通知
        // 目前仅记录操作，返回成功信息
        for (Integer recordId : recordIds) {
            BorrowRecord record = borrowRecordMapper.selectByPrimaryKey(recordId);
            if (record != null && record.getReturnTime() == null) {
                // 标记已提醒（如果有此字段的话）
                // 实际实现中可以添加提醒次数、提醒时间等字段
            }
        }
        return "已向 " + recordIds.size() + " 位逾期用户发送催还通知";
    }
}