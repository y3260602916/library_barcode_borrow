package com.campus.libraryborrowbackend.service.impl;

import com.campus.libraryborrowbackend.entity.Book;
import com.campus.libraryborrowbackend.entity.BookExample;
import com.campus.libraryborrowbackend.entity.InventoryRecord;
import com.campus.libraryborrowbackend.entity.InventoryRecordExample;
import com.campus.libraryborrowbackend.mapper.BookMapper;
import com.campus.libraryborrowbackend.mapper.InventoryRecordMapper;
import com.campus.libraryborrowbackend.service.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class InventoryServiceImpl implements InventoryService {

    @Autowired
    private InventoryRecordMapper inventoryRecordMapper;

    @Autowired
    private BookMapper bookMapper;

    // 入库类型
    private static final int TYPE_IN = 1;
    // 出库类型
    private static final int TYPE_OUT = 2;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String stockIn(Integer bookId, String bookBarcode, Integer quantity, String operator, String remark,
                          String bookName, String author, String category, String price) {
        Book book = null;
        
        // 优先通过bookId查询
        if (bookId != null) {
            book = bookMapper.selectByPrimaryKey(bookId);
        }
        // 如果没有bookId，尝试通过条码查询
        else if (bookBarcode != null && !bookBarcode.trim().isEmpty()) {
            BookExample example = new BookExample();
            example.createCriteria().andBookBarcodeEqualTo(bookBarcode.trim());
            List<Book> books = bookMapper.selectByExample(example);
            if (!books.isEmpty()) {
                book = books.get(0);
            }
        }
        
        // 如果图书不存在，尝试创建新图书
        if (book == null) {
            if (bookBarcode == null || bookBarcode.trim().isEmpty()) {
                throw new RuntimeException("新增图书入库时，条码号不能为空");
            }
            if (bookName == null || bookName.trim().isEmpty()) {
                throw new RuntimeException("新增图书入库时，图书名称不能为空");
            }
            
            // 创建新图书
            book = new Book();
            book.setBookBarcode(bookBarcode.trim());
            book.setBookName(bookName.trim());
            book.setAuthor(author != null ? author.trim() : null);
            book.setCategory(category != null ? category.trim() : null);
            if (price != null && !price.trim().isEmpty()) {
                try {
                    book.setPrice(java.math.BigDecimal.valueOf(Double.parseDouble(price.trim())));
                } catch (Exception e) {
                    // 价格格式错误，忽略
                }
            }
            book.setTotalStock(quantity);
            book.setRemainStock(quantity);
            bookMapper.insert(book);
            
            // 获取新插入图书的ID
            BookExample example = new BookExample();
            example.createCriteria().andBookBarcodeEqualTo(bookBarcode.trim());
            List<Book> books = bookMapper.selectByExample(example);
            if (!books.isEmpty()) {
                book = books.get(0);
            }
        } else {
            // 更新图书库存（总量和剩余量都增加）
            book.setTotalStock(book.getTotalStock() + quantity);
            book.setRemainStock(book.getRemainStock() + quantity);
            bookMapper.updateByPrimaryKey(book);
        }

        // 记录出入库流水
        InventoryRecord record = new InventoryRecord();
        record.setBookId(book.getBookId());
        record.setBookName(book.getBookName());
        record.setType(TYPE_IN);
        record.setQuantity(quantity);
        record.setOperator(operator);
        record.setOperateTime(new Date());
        record.setRemark(remark);
        inventoryRecordMapper.insert(record);

        return bookId == null ? "图书新增并入库成功" : "入库成功";
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String stockOut(Integer bookId, Integer quantity, String operator, String remark) {
        Book book = bookMapper.selectByPrimaryKey(bookId);
        if (book == null) {
            throw new RuntimeException("图书不存在");
        }

        if (book.getTotalStock() < quantity) {
            throw new RuntimeException("出库数量超过库存总量");
        }

        // 更新图书库存（总量减少，剩余量也减少）
        book.setTotalStock(book.getTotalStock() - quantity);
        book.setRemainStock(book.getRemainStock() - quantity);
        bookMapper.updateByPrimaryKey(book);

        // 记录出入库流水
        InventoryRecord record = new InventoryRecord();
        record.setBookId(bookId);
        record.setBookName(book.getBookName());
        record.setType(TYPE_OUT);
        record.setQuantity(quantity);
        record.setOperator(operator);
        record.setOperateTime(new Date());
        record.setRemark(remark);
        inventoryRecordMapper.insert(record);

        return "出库成功";
    }

    @Override
    public List<Map<String, Object>> getInventoryRecords(Integer bookId, Integer type) {
        List<Map<String, Object>> result = new ArrayList<>();

        InventoryRecordExample example = new InventoryRecordExample();
        InventoryRecordExample.Criteria criteria = example.createCriteria();

        if (bookId != null) {
            criteria.andBookIdEqualTo(bookId);
        }
        if (type != null) {
            criteria.andTypeEqualTo(type);
        }

        // 按操作时间倒序排列
        example.setOrderByClause("operate_time DESC");

        List<InventoryRecord> records = inventoryRecordMapper.selectByExample(example);

        for (InventoryRecord record : records) {
            Map<String, Object> item = new HashMap<>();
            item.put("recordId", record.getRecordId());
            item.put("bookId", record.getBookId());
            item.put("bookName", record.getBookName());
            item.put("type", record.getType() == TYPE_IN ? "IN" : "OUT");
            item.put("typeName", record.getType() == TYPE_IN ? "入库" : "出库");
            item.put("quantity", record.getQuantity());
            item.put("operator", record.getOperator());
            item.put("operateTime", record.getOperateTime());
            item.put("remark", record.getRemark());
            result.add(item);
        }

        return result;
    }
}