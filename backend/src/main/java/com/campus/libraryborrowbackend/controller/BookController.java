package com.campus.libraryborrowbackend.controller;

import com.campus.libraryborrowbackend.common.Result;
import com.campus.libraryborrowbackend.entity.Book;
import com.campus.libraryborrowbackend.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/book")
public class BookController {

    @Autowired
    private BookService bookService;

    /**
     * 图书列表查询 + 关键词搜索 + 分类筛选 + 排序
     * 接口地址：GET /book/list?keyword=xxx&category=xxx&sortField=xxx&sortOrder=xxx
     */
    @GetMapping("/list")
    public Result<List<Book>> getBookList(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String category,
            @RequestParam(required = false, defaultValue = "bookId") String sortField,
            @RequestParam(required = false, defaultValue = "asc") String sortOrder) {
        List<Book> bookList = bookService.getBookList(keyword, category, sortField, sortOrder);
        return Result.success(bookList);
    }

    /**
     * 获取图书详情（包含库存信息）
     * 接口地址：GET /book/detail/{bookId}
     */
    @GetMapping("/detail/{bookId}")
    public Result<Map<String, Object>> getBookDetail(@PathVariable Integer bookId) {
        Map<String, Object> bookDetail = bookService.getBookDetail(bookId);
        if (bookDetail == null) {
            return Result.error("图书不存在");
        }
        return Result.success(bookDetail);
    }

    /**
     * 调整图书库存
     * 接口地址：PUT /book/adjust-stock
     */
    @PutMapping("/adjust-stock")
    public Result<String> adjustStock(@RequestBody Map<String, Object> params) {
        try {
            Integer bookId = Integer.parseInt(params.get("bookId").toString());
            Integer newStock = Integer.parseInt(params.get("newStock").toString());
            String remark = params.get("remark") != null ? params.get("remark").toString() : "";

            String msg = bookService.adjustStock(bookId, newStock, remark);
            return Result.success(msg);
        } catch (RuntimeException e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 获取全馆图书库存（支持关键词搜索和分类筛选）
     * 接口地址：GET /book/stock?keyword=xxx&category=xxx
     */
    @GetMapping("/stock")
    public Result<List<Map<String, Object>>> getAllBookStock(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String category) {
        List<Map<String, Object>> stockList = bookService.getAllBookStock(keyword, category);
        return Result.success(stockList);
    }
}