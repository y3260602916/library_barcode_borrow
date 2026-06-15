package com.campus.libraryborrowbackend.service;

import com.campus.libraryborrowbackend.entity.Book;
import java.util.List;
import java.util.Map;

public interface BookService {

    /**
     * 分页/条件查询图书列表（支持关键词搜索、分类筛选、排序）
     * @param keyword 搜索关键词
     * @param category 图书分类
     * @param sortField 排序字段
     * @param sortOrder 排序方式（asc/desc）
     * @return 图书集合
     */
    List<Book> getBookList(String keyword, String category, String sortField, String sortOrder);

    /**
     * 获取图书详情（包含库存信息）
     * @param bookId 图书ID
     * @return 图书详情Map
     */
    Map<String, Object> getBookDetail(Integer bookId);

    /**
     * 调整图书库存
     * @param bookId 图书ID
     * @param newStock 新库存数量
     * @param remark 备注
     * @return 操作结果
     */
    String adjustStock(Integer bookId, Integer newStock, String remark);

    /**
     * 获取全馆图书库存（支持关键词搜索和分类筛选）
     * @param keyword 搜索关键词（书名或作者）
     * @param category 图书分类
     * @return 库存列表
     */
    List<Map<String, Object>> getAllBookStock(String keyword, String category);
}