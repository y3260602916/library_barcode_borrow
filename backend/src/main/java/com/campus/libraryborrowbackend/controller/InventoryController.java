package com.campus.libraryborrowbackend.controller;

import com.campus.libraryborrowbackend.common.Result;
import com.campus.libraryborrowbackend.service.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/inventory")
public class InventoryController {

    @Autowired
    private InventoryService inventoryService;

    /**
     * 图书入库（支持新增图书入库）
     * 地址：POST /inventory/stock-in
     */
    @PostMapping("/stock-in")
    public Result<String> stockIn(@RequestBody Map<String, Object> params) {
        try {
            Integer bookId = params.get("bookId") != null ? Integer.parseInt(params.get("bookId").toString()) : null;
            String bookBarcode = params.get("bookBarcode") != null ? params.get("bookBarcode").toString() : null;
            Integer quantity = Integer.parseInt(params.get("quantity").toString());
            String operator = params.get("operator").toString();
            String remark = params.get("remark") != null ? params.get("remark").toString() : "";
            
            // 新增图书的信息（用于创建新书）
            String bookName = params.get("bookName") != null ? params.get("bookName").toString() : null;
            String author = params.get("author") != null ? params.get("author").toString() : null;
            String category = params.get("category") != null ? params.get("category").toString() : null;
            String price = params.get("price") != null ? params.get("price").toString() : null;

            String msg = inventoryService.stockIn(bookId, bookBarcode, quantity, operator, remark, bookName, author, category, price);
            return Result.success(msg);
        } catch (RuntimeException e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 图书出库
     * 地址：POST /inventory/stock-out
     */
    @PostMapping("/stock-out")
    public Result<String> stockOut(@RequestBody Map<String, Object> params) {
        try {
            Integer bookId = Integer.parseInt(params.get("bookId").toString());
            Integer quantity = Integer.parseInt(params.get("quantity").toString());
            String operator = params.get("operator").toString();
            String remark = params.get("remark") != null ? params.get("remark").toString() : "";

            String msg = inventoryService.stockOut(bookId, quantity, operator, remark);
            return Result.success(msg);
        } catch (RuntimeException e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 查询出入库流水
     * 地址：GET /inventory/records
     */
    @GetMapping("/records")
    public Result<List<Map<String, Object>>> getInventoryRecords(
            @RequestParam(required = false) Integer bookId,
            @RequestParam(required = false) Integer type) {
        List<Map<String, Object>> records = inventoryService.getInventoryRecords(bookId, type);
        return Result.success(records);
    }
}