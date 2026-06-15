package com.campus.libraryborrowbackend.service;

import com.campus.libraryborrowbackend.entity.InventoryRecord;

import java.util.List;
import java.util.Map;

public interface InventoryService {

    /**
     * 图书入库（支持新增图书入库）
     * @param bookId 图书ID（可为null，此时通过barcode查询或创建新书）
     * @param bookBarcode 图书条码（可为null，此时通过bookId查询）
     * @param quantity 入库数量
     * @param operator 操作人
     * @param remark 备注
     * @param bookName 图书名称（新增图书时必填）
     * @param author 作者（新增图书时选填）
     * @param category 分类（新增图书时选填）
     * @param price 价格（新增图书时选填）
     */
    String stockIn(Integer bookId, String bookBarcode, Integer quantity, String operator, String remark, 
                   String bookName, String author, String category, String price);

    /**
     * 图书出库
     */
    String stockOut(Integer bookId, Integer quantity, String operator, String remark);

    /**
     * 查询出入库流水
     */
    List<Map<String, Object>> getInventoryRecords(Integer bookId, Integer type);
}