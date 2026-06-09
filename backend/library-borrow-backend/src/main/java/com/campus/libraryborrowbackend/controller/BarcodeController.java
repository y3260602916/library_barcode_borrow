package com.campus.libraryborrowbackend.controller;

import com.campus.libraryborrowbackend.common.Result;
import com.campus.libraryborrowbackend.entity.Book;
import com.campus.libraryborrowbackend.entity.ScanRecord;
import com.campus.libraryborrowbackend.mapper.BookMapper;
import com.campus.libraryborrowbackend.mapper.ScanRecordMapper;
import com.campus.libraryborrowbackend.util.PythonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

@RestController
@RequestMapping("/barcode")
public class BarcodeController {

    @Value("${python.upload-path}")
    private String uploadPath;

    @Autowired
    private PythonUtil pythonUtil;

    @Autowired
    private BookMapper bookMapper;

    @Autowired
    private ScanRecordMapper scanRecordMapper;

    /**
     * 图片上传 + 条码识别 + 图书查询 + 识别日志入库
     */
    @PostMapping("/upload")
    public Result<Book> uploadImage(@RequestParam("file") MultipartFile file) {
        // 1. 校验文件
        if (file.isEmpty()) {
            return Result.error("请上传图片文件");
        }

        // 2. 保存文件到本地
        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }
        String suffix = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
        String fileName = UUID.randomUUID() + "_" + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + suffix;
        File targetFile = new File(uploadPath, fileName);
        String imgFullPath = targetFile.getAbsolutePath();

        try {
            file.transferTo(targetFile);
        } catch (Exception e) {
            // 文件保存失败，记录日志
            ScanRecord failRecord = new ScanRecord();
            failRecord.setImgPath(imgFullPath);
            failRecord.setScanBarcode("");
            failRecord.setScanTime(new Date());
            failRecord.setResult("失败");
            scanRecordMapper.insert(failRecord);
            return Result.error("文件保存失败：" + e.getMessage());
        }

        // 3. 调用Python脚本识别条码
        String barcode = pythonUtil.getBarcode(targetFile.getAbsolutePath());
        if (barcode == null || barcode.trim().isEmpty()) {
            ScanRecord failRecord = new ScanRecord();
            failRecord.setImgPath(imgFullPath);
            failRecord.setScanBarcode("");
            failRecord.setScanTime(new Date());
            failRecord.setResult("失败");
            scanRecordMapper.insert(failRecord);
            return Result.error("条码识别失败，请重新上传清晰的图片");
        }

        // 过滤非数字字符
        barcode = barcode.trim();
        barcode = barcode.replaceAll("[^0-9]", "");
        System.out.println("最终用于查询的条码：=====" + barcode + "=====");

        if (barcode.isEmpty()) {
            ScanRecord failRecord = new ScanRecord();
            failRecord.setImgPath(imgFullPath);
            failRecord.setScanBarcode("");
            failRecord.setScanTime(new Date());
            failRecord.setResult("失败");
            scanRecordMapper.insert(failRecord);
            return Result.error("未识别到有效条码");
        }

        // 4. 识别成功，写入日志
        ScanRecord successRecord = new ScanRecord();
        successRecord.setImgPath(imgFullPath);
        successRecord.setScanBarcode(barcode);
        successRecord.setScanTime(new Date());
        successRecord.setResult("成功");
        scanRecordMapper.insert(successRecord);

        // 5. 查询图书
        Book book = bookMapper.selectBookByBarcode(barcode);
        if (book == null) {
            return Result.error("未找到条码为 " + barcode + " 的图书信息");
        }

        return Result.success(book);
    }
}