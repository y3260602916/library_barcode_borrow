package com.campus.libraryborrowbackend.service.impl;

import com.campus.libraryborrowbackend.entity.Book;
import com.campus.libraryborrowbackend.entity.ScanRecord;
import com.campus.libraryborrowbackend.mapper.BookMapper;
import com.campus.libraryborrowbackend.mapper.ScanRecordMapper;
import com.campus.libraryborrowbackend.service.BarcodeService;
import com.campus.libraryborrowbackend.util.PythonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.Date;

@Service
public class BarcodeServiceImpl implements BarcodeService {

    @Autowired
    private PythonUtil pythonUtil;

    @Autowired
    private BookMapper bookMapper;

    @Autowired
    private ScanRecordMapper scanRecordMapper;

    @Override
    public Book uploadAndRecognize(MultipartFile file) {
        // 1. 文件校验
        if (file.isEmpty()) {
            throw new RuntimeException("请上传图片文件");
        }

        // 2. 临时保存文件（供Python识别使用）
        File tempFile = null;
        try {
            // 创建临时文件
            tempFile = File.createTempFile("barcode_", ".jpg");
            file.transferTo(tempFile);

            // 3. 调用Python识别条码
            String barcode = pythonUtil.getBarcode(tempFile.getAbsolutePath());
            
            // 4. 清理临时文件
            tempFile.delete();
            
            if (barcode == null || barcode.trim().isEmpty()) {
                throw new RuntimeException("条码识别失败，请重新上传清晰的图片");
            }

            // 过滤非数字
            barcode = barcode.trim().replaceAll("[^0-9]", "");
            System.out.println("最终用于查询的条码：=====" + barcode + "=====");

            if (barcode.isEmpty()) {
                throw new RuntimeException("未识别到有效条码");
            }

            // 5. 识别成功，写入日志（仅记录识别信息，不再记录图片路径）
            ScanRecord successRecord = new ScanRecord();
            successRecord.setScanBarcode(barcode);
            successRecord.setScanTime(new Date());
            successRecord.setResult("成功");
            scanRecordMapper.insert(successRecord);

            // 6. 查询图书
            Book book = bookMapper.selectBookByBarcode(barcode);
            if (book == null) {
                throw new RuntimeException("未找到条码为 " + barcode + " 的图书信息");
            }
            return book;
            
        } catch (RuntimeException e) {
            // 业务异常直接抛出
            throw e;
        } catch (Exception e) {
            // 清理临时文件
            if (tempFile != null && tempFile.exists()) {
                tempFile.delete();
            }
            throw new RuntimeException("文件处理失败：" + e.getMessage());
        }
    }

    @Override
    public Book getBookByBarcode(String barcode) {
        if (barcode == null || barcode.trim().isEmpty()) {
            throw new RuntimeException("图书条码不能为空");
        }
        Book book = bookMapper.selectBookByBarcode(barcode.trim());
        if (book == null) {
            throw new RuntimeException("未找到该条码对应的图书");
        }
        return book;
    }
}