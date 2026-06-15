package com.campus.libraryborrowbackend.service;

import com.campus.libraryborrowbackend.entity.Book;
import org.springframework.web.multipart.MultipartFile;

public interface BarcodeService {

    /**
     * 图片上传 + 条码识别 + 日志记录 + 图书查询
     */
    Book uploadAndRecognize(MultipartFile file);

    /**
     * 根据条码查询图书
     */
    Book getBookByBarcode(String barcode);
}