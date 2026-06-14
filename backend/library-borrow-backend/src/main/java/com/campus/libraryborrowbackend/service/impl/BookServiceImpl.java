package com.campus.libraryborrowbackend.service.impl;

import com.campus.libraryborrowbackend.entity.Book;
import com.campus.libraryborrowbackend.entity.BookExample;
import com.campus.libraryborrowbackend.entity.BorrowRecord;
import com.campus.libraryborrowbackend.entity.BorrowRecordExample;
import com.campus.libraryborrowbackend.mapper.BookMapper;
import com.campus.libraryborrowbackend.mapper.BorrowRecordMapper;
import com.campus.libraryborrowbackend.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BookServiceImpl implements BookService {

    @Autowired
    private BookMapper bookMapper;

    @Autowired
    private BorrowRecordMapper borrowRecordMapper;

    @Override
    public List<Book> getBookList(String keyword, String category, String sortField, String sortOrder) {
        BookExample example = new BookExample();
        
        // 设置排序 - 需要将前端的驼峰命名转换为数据库的下划线命名
        if (sortField != null && !sortField.trim().isEmpty()) {
            // 字段名映射：前端驼峰 -> 数据库下划线
            String dbField = convertToDbField(sortField.trim());
            String orderClause = dbField + " " + ("desc".equalsIgnoreCase(sortOrder) ? "DESC" : "ASC");
            example.setOrderByClause(orderClause);
        }
        
        return bookMapper.selectByExample(example);
    }
    
    /**
     * 将前端的驼峰命名字段转换为数据库的下划线命名
     */
    private String convertToDbField(String camelCaseField) {
        // 字段名映射表
        Map<String, String> fieldMapping = new HashMap<>();
        fieldMapping.put("bookId", "book_id");
        fieldMapping.put("bookName", "book_name");
        fieldMapping.put("author", "author");
        fieldMapping.put("category", "category");
        fieldMapping.put("bookBarcode", "book_barcode");
        fieldMapping.put("totalStock", "total_stock");
        fieldMapping.put("remainStock", "remain_stock");
        
        // 如果有映射则返回映射值，否则直接返回原值（作为安全回退）
        return fieldMapping.getOrDefault(camelCaseField, camelCaseField);
    }

    @Override
    public Map<String, Object> getBookDetail(Integer bookId) {
        // 查询图书基本信息
        Book book = bookMapper.selectByPrimaryKey(bookId);
        if (book == null) {
            return null;
        }

        // 查询该图书的已借出数量（未归还的）
        BorrowRecordExample example = new BorrowRecordExample();
        example.createCriteria()
                .andBookIdEqualTo(bookId)
                .andReturnTimeIsNull();
        List<BorrowRecord> borrowingRecords = borrowRecordMapper.selectByExample(example);
        int borrowedCount = borrowingRecords.size();

        // 计算剩余库存
        int totalStock = book.getTotalStock() != null ? book.getTotalStock() : 0;
        int remainStock = totalStock - borrowedCount;

        // 构建返回结果
        Map<String, Object> result = new HashMap<>();
        result.put("book", book);
        result.put("totalStock", totalStock);
        result.put("borrowedCount", borrowedCount);
        result.put("remainStock", remainStock);

        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String adjustStock(Integer bookId, Integer newStock, String remark) {
        Book book = bookMapper.selectByPrimaryKey(bookId);
        if (book == null) {
            throw new RuntimeException("图书不存在");
        }

        if (newStock < 0) {
            throw new RuntimeException("库存数量不能为负数");
        }

        // 查询该图书的已借出数量（未归还的）
        BorrowRecordExample example = new BorrowRecordExample();
        example.createCriteria()
                .andBookIdEqualTo(bookId)
                .andReturnTimeIsNull();
        List<BorrowRecord> borrowingRecords = borrowRecordMapper.selectByExample(example);
        int borrowedCount = borrowingRecords.size();

        if (newStock < borrowedCount) {
            throw new RuntimeException("库存数量不能小于已借出数量");
        }

        // 更新库存
        book.setTotalStock(newStock);
        book.setRemainStock(newStock - borrowedCount);
        bookMapper.updateByPrimaryKey(book);

        return "库存调整成功";
    }

    @Override
    public List<Map<String, Object>> getAllBookStock() {
        List<Map<String, Object>> result = new ArrayList<>();

        BookExample example = new BookExample();
        List<Book> books = bookMapper.selectByExample(example);

        for (Book book : books) {
            // 查询该图书的已借出数量（未归还的）
            BorrowRecordExample borrowExample = new BorrowRecordExample();
            borrowExample.createCriteria()
                    .andBookIdEqualTo(book.getBookId())
                    .andReturnTimeIsNull();
            List<BorrowRecord> borrowingRecords = borrowRecordMapper.selectByExample(borrowExample);
            int borrowedCount = borrowingRecords.size();

            int totalStock = book.getTotalStock() != null ? book.getTotalStock() : 0;
            int remainStock = totalStock - borrowedCount;

            Map<String, Object> item = new HashMap<>();
            item.put("bookId", book.getBookId());
            item.put("bookName", book.getBookName());
            item.put("author", book.getAuthor());
            item.put("category", book.getCategory());
            item.put("totalStock", totalStock);
            item.put("borrowedCount", borrowedCount);
            item.put("remainStock", remainStock);
            result.add(item);
        }

        return result;
    }
}