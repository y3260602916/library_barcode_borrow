package com.campus.libraryborrowbackend.controller;

import com.campus.libraryborrowbackend.common.Result;
import com.campus.libraryborrowbackend.entity.Book;
import com.campus.libraryborrowbackend.service.BarcodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/barcode")
public class BarcodeController {

    @Autowired
    private BarcodeService barcodeService;

    /**
     * 图片上传 + 条码识别
     */
    @PostMapping("/upload")
    public Result<Book> uploadImage(@RequestParam("file") MultipartFile file) {
        try {
            Book book = barcodeService.uploadAndRecognize(file);
            return Result.success(book);
        } catch (RuntimeException e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 根据条码查询图书信息
     */
    @GetMapping("/book")
    public Result<Book> getBookByBarcode(@RequestParam String barcode) {
        try {
            Book book = barcodeService.getBookByBarcode(barcode);
            return Result.success(book);
        } catch (RuntimeException e) {
            return Result.error(e.getMessage());
        }
    }
}