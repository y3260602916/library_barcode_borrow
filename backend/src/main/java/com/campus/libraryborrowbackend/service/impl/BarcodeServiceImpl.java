package com.campus.libraryborrowbackend.service.impl;

import com.campus.libraryborrowbackend.entity.Book;
import com.campus.libraryborrowbackend.entity.ScanRecord;
import com.campus.libraryborrowbackend.mapper.BookMapper;
import com.campus.libraryborrowbackend.mapper.ScanRecordMapper;
import com.campus.libraryborrowbackend.service.BarcodeService;
import com.campus.libraryborrowbackend.util.PythonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

@Service
public class BarcodeServiceImpl implements BarcodeService {

    @Value("${python.upload-path}")
    private String uploadPath;

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

        // 2. 创建上传目录
        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }

        // 拼接文件名
        String suffix = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
        String fileName = UUID.randomUUID() + "_" + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + suffix;
        File targetFile = new File(uploadPath, fileName);
        String imgFullPath = targetFile.getAbsolutePath();

        // 3. 保存文件
        try {
            file.transferTo(targetFile);
        } catch (Exception e) {
            // 保存失败，写入日志
            ScanRecord failRecord = new ScanRecord();
            failRecord.setImgPath(imgFullPath);
            failRecord.setScanBarcode("");
            failRecord.setScanTime(new Date());
            failRecord.setResult("失败");
            scanRecordMapper.insert(failRecord);
            throw new RuntimeException("文件保存失败：" + e.getMessage());
        }

        // 4. 调用Python识别条码
        String barcode = pythonUtil.getBarcode(targetFile.getAbsolutePath());
        if (barcode == null || barcode.trim().isEmpty()) {
            ScanRecord failRecord = new ScanRecord();
            failRecord.setImgPath(imgFullPath);
            failRecord.setScanBarcode("");
            failRecord.setScanTime(new Date());
            failRecord.setResult("失败");
            scanRecordMapper.insert(failRecord);
            throw new RuntimeException("条码识别失败，请重新上传清晰的图片");
        }

        // 过滤非数字
        barcode = barcode.trim().replaceAll("[^0-9]", "");
        System.out.println("最终用于查询的条码：=====" + barcode + "=====");

        if (barcode.isEmpty()) {
            ScanRecord failRecord = new ScanRecord();
            failRecord.setImgPath(imgFullPath);
            failRecord.setScanBarcode("");
            failRecord.setScanTime(new Date());
            failRecord.setResult("失败");
            scanRecordMapper.insert(failRecord);
            throw new RuntimeException("未识别到有效条码");
        }

        // 5. 识别成功，写入日志
        ScanRecord successRecord = new ScanRecord();
        successRecord.setImgPath(imgFullPath);
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