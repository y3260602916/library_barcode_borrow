package com.campus.libraryborrowbackend.service;

import com.alibaba.fastjson2.JSON;
import com.campus.libraryborrowbackend.dto.AiBookDTO;
import com.campus.libraryborrowbackend.entity.Book;
import com.campus.libraryborrowbackend.entity.BookExample;
import com.campus.libraryborrowbackend.entity.BorrowRecord;
import com.campus.libraryborrowbackend.entity.BorrowRecordExample;
import com.campus.libraryborrowbackend.mapper.BookMapper;
import com.campus.libraryborrowbackend.mapper.BorrowRecordMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class StatsService {

    @Autowired
    private BorrowRecordMapper borrowRecordMapper;

    @Autowired
    private BookMapper bookMapper;

    @Value("${python.python-path}")
    private String pythonPath;
    @Value("${python.script-path}")
    private String scriptPath;

    // 1. 基础统计数据（只使用你Mapper里存在的方法）
    public Map<String, Object> getOverview(Integer userId) {
        Map<String, Object> map = new HashMap<>();
        try {
            // 获取所有图书 - 使用简化查询方法
            List<Book> allBooks = bookMapper.selectAllBooks();
            System.out.println("[DEBUG] 图书查询结果数量: " + (allBooks != null ? allBooks.size() : "null"));
            
            // 馆藏图书种类数（不同图书的数量）
            map.put("bookTypes", allBooks != null ? allBooks.size() : 0);
            
            // 馆藏图书总量（所有图书库存之和），处理可能的null值
            int totalStock = 0;
            if (allBooks != null && !allBooks.isEmpty()) {
                for (Book book : allBooks) {
                    if (book != null && book.getTotalStock() != null) {
                        totalStock += book.getTotalStock();
                    }
                }
            }
            map.put("totalStock", totalStock);
        } catch (Exception e) {
            System.out.println("[ERROR] 查询图书数据异常: " + e.getMessage());
            e.printStackTrace();
            map.put("bookTypes", 0);
            map.put("totalStock", 0);
        }
        
        try {
            // 当前用户未归还图书数
            System.out.println("[DEBUG] 查询用户 " + userId + " 的未归还图书");
            BorrowRecordExample borrowExample = new BorrowRecordExample();
            borrowExample.createCriteria().andUserIdEqualTo(userId).andReturnTimeIsNull();
            List<BorrowRecord> unreturnedList = borrowRecordMapper.selectByExample(borrowExample);
            int unreturnedCount = unreturnedList != null ? unreturnedList.size() : 0;
            System.out.println("[DEBUG] 用户 " + userId + " 的未归还图书数量: " + unreturnedCount);
            map.put("unreturnedBooks", unreturnedCount);
        } catch (Exception e) {
            System.out.println("[ERROR] 查询借阅记录异常: " + e.getMessage());
            e.printStackTrace();
            map.put("unreturnedBooks", 0);
        }
        
        try {
            // 当前用户借阅数
            BorrowRecordExample userExample = new BorrowRecordExample();
            userExample.createCriteria().andUserIdEqualTo(userId);
            map.put("userBorrowCount", borrowRecordMapper.selectByExample(userExample).size());
        } catch (Exception e) {
            System.out.println("[ERROR] 查询用户借阅记录异常: " + e.getMessage());
            map.put("userBorrowCount", 0);
        }
        
        return map;
    }

    // 2. 图表数据（从数据库查询真实数据）
    public Map<String, Object> getCharts() {
        Map<String, Object> map = new HashMap<>();
        
        // 近 7 天借阅趋势（示例数据）
        Map<String, Object> trend = new HashMap<>();
        trend.put("days", Arrays.asList("周一", "周二", "周三", "周四", "周五", "周六", "周日"));
        trend.put("counts", Arrays.asList(1, 3, 2, 5, 4, 6, 2));
        map.put("trend", trend);
        
        // 图书分类占比（从数据库查询真实数据）- 使用简化查询方法
        List<Book> allBooks = bookMapper.selectAllBooks();
        Map<String, Integer> categoryCount = new HashMap<>();
        
        if (allBooks != null && !allBooks.isEmpty()) {
            for (Book book : allBooks) {
                String category = book.getCategory();
                if (category != null && !category.isEmpty()) {
                    categoryCount.put(category, categoryCount.getOrDefault(category, 0) + 1);
                }
            }
        }
        
        // 转换为 ECharts 需要的格式
        List<Map<String, Object>> categoryList = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : categoryCount.entrySet()) {
            Map<String, Object> item = new HashMap<>();
            item.put("name", entry.getKey());
            item.put("value", entry.getValue());
            categoryList.add(item);
        }
        
        map.put("category", categoryList);
        return map;
    }

    // 3. 常规推荐图书（只使用你已有的selectRecommendBooks）
    public List<Book> getRecommendBooks(Integer userId) {
        BorrowRecordExample example = new BorrowRecordExample();
        example.createCriteria().andUserIdEqualTo(userId);
        List<BorrowRecord> records = borrowRecordMapper.selectByExample(example);

        // 方案A：冷启动或没查到偏好时，直接返回热门图书
        if (records == null || records.isEmpty()) {
            // 直接查所有图书，按库存排序，取前5本，确保有数据
            List<Book> list = bookMapper.selectAllBooks();
            // 手动排序
            if (list != null) {
                list.sort((a, b) -> {
                    int stockA = a.getTotalStock() != null ? a.getTotalStock() : 0;
                    int stockB = b.getTotalStock() != null ? b.getTotalStock() : 0;
                    return Integer.compare(stockB, stockA); // 降序
                });
            }
            return list != null && list.size() > 5 ? list.subList(0, 5) : list;
        }

        // 方案B：有借阅记录，按分类推荐
        Map<String, Integer> categoryCount = new HashMap<>();
        for (BorrowRecord rec : records) {
            Book book = bookMapper.selectByPrimaryKey(rec.getBookId());
            if (book != null && book.getCategory() != null) {
                categoryCount.put(book.getCategory(), categoryCount.getOrDefault(book.getCategory(), 0) + 1);
            }
        }

        List<String> topCategories = categoryCount.entrySet().stream()
                .sorted((a, b) -> b.getValue().compareTo(a.getValue()))
                .limit(2)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        // 如果分类为空，直接走兜底查询
        if (topCategories.isEmpty()) {
            BookExample bookExample = new BookExample();
            bookExample.setOrderByClause("total_stock DESC");
            List<Book> list = bookMapper.selectByExample(bookExample);
            return list.size() > 5 ? list.subList(0, 5) : list;
        }

        // 调用你原来的推荐方法
        List<Book> recommendList = bookMapper.selectRecommendBooks(topCategories, userId, 5);

        // 关键兜底：如果推荐方法还是返回空，直接返回所有图书的前5本
        if (recommendList.isEmpty()) {
            BookExample bookExample = new BookExample();
            bookExample.setOrderByClause("total_stock DESC");
            List<Book> list = bookMapper.selectByExample(bookExample);
            return list.size() > 5 ? list.subList(0, 5) : list;
        }

        return recommendList;
    }

    // 4. AI 图书推荐核心方法（修复泛型和 final 问题）
    public List<AiBookDTO> getAiRecommend(Integer userId) {
        List<BorrowRecord> borrowRecords = borrowRecordMapper.selectByUserId(userId);
        List<Book> bookList;
        String userHistory = "";

        // 冷启动：无借阅记录 → 直接查询所有图书
        if (borrowRecords == null || borrowRecords.isEmpty()) {
            // 直接查所有图书，兜底确保有数据
            List<Book> list = bookMapper.selectAllBooks();
            // 手动排序
            if (list != null) {
                list.sort((a, b) -> {
                    int stockA = a.getTotalStock() != null ? a.getTotalStock() : 0;
                    int stockB = b.getTotalStock() != null ? b.getTotalStock() : 0;
                    return Integer.compare(stockB, stockA); // 降序
                });
            }
            bookList = list != null && list.size() > 5 ? list.subList(0, 5) : list;
        } else {
            // 拼接用户阅读历史
            userHistory = borrowRecords.stream()
                    .map(rec -> {
                        Book book = bookMapper.selectByPrimaryKey(rec.getBookId());
                        return book == null ? "" : book.getBookName();
                    })
                    .filter(s -> !s.isEmpty())
                    .collect(Collectors.joining("、"));

            // 统计分类偏好
            Map<String, Integer> categoryCount = new HashMap<>();
            for (BorrowRecord rec : borrowRecords) {
                Book book = bookMapper.selectByPrimaryKey(rec.getBookId());
                if (book != null && book.getCategory() != null) {
                    categoryCount.put(book.getCategory(),
                            categoryCount.getOrDefault(book.getCategory(), 0) + 1);
                }
            }

            List<String> topCategories = categoryCount.entrySet().stream()
                    .sorted((a, b) -> b.getValue().compareTo(a.getValue()))
                    .limit(2)
                    .map(Map.Entry::getKey)
                    .collect(Collectors.toList());

            // 使用项目原有分类推荐方法
            bookList = bookMapper.selectRecommendBooks(topCategories, userId, 5);
        }

        // 如果 bookList 为空，查询所有图书作为兜底
        if (bookList == null || bookList.isEmpty()) {
            List<Book> list = bookMapper.selectAllBooks();
            // 手动排序
            if (list != null) {
                list.sort((a, b) -> {
                    int stockA = a.getTotalStock() != null ? a.getTotalStock() : 0;
                    int stockB = b.getTotalStock() != null ? b.getTotalStock() : 0;
                    return Integer.compare(stockB, stockA); // 降序
                });
            }
            bookList = list != null && list.size() > 5 ? list.subList(0, 5) : list;
        }

        // 如果还是空，直接返回空列表
        if (bookList == null || bookList.isEmpty()) {
            return new ArrayList<>();
        }

        // 调用本地 Python 脚本 recommend 逻辑
        String pythonOutput = "";
        Process process = null;
        try {
            String[] cmd = {pythonPath, scriptPath, "recommend"};
            process = new ProcessBuilder(cmd)
                    .redirectErrorStream(true)
                    .start();

            // 向标准输入传入阅读历史
            try (BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(process.getOutputStream(), StandardCharsets.UTF_8))) {
                writer.write(userHistory);
                writer.flush();
            }

            // 读取 Python 输出，只收集 JSON 数组
            StringBuilder sb = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream(), StandardCharsets.UTF_8))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String trimLine = line.trim();
                    System.out.println("[Python 输出] " + trimLine);
                    if (trimLine.startsWith("[") && trimLine.endsWith("]")) {
                        sb.append(trimLine);
                    }
                }
            }

            // 设置超时 10 秒，防止进程卡死
            if (process.waitFor(10, TimeUnit.SECONDS) && sb.length() > 0) {
                pythonOutput = sb.toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (process != null) {
                process.destroy();
            }
        }

        // 解析 JSON 并封装 DTO（修复 final 和泛型问题）
        final List<Map> aiResultList = new ArrayList<>();
        try {
            if (pythonOutput != null && !pythonOutput.isEmpty() && pythonOutput.startsWith("[") && pythonOutput.endsWith("]")) {
                aiResultList.addAll(JSON.parseArray(pythonOutput, Map.class));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 组装返回数据：优先使用Python返回的推荐结果
        List<AiBookDTO> result = new ArrayList<>();
        
        // 如果Python返回了推荐结果，直接使用
        if (!aiResultList.isEmpty()) {
            for (Map item : aiResultList) {
                AiBookDTO dto = new AiBookDTO();
                dto.setBookName((String) item.get("bookName"));
                dto.setAuthor((String) item.get("author"));
                dto.setCategory((String) item.get("category"));
                dto.setReason((String) item.getOrDefault("reason", "结合您的阅读偏好，为您推荐图书"));
                result.add(dto);
            }
        } else {
            // 兜底：使用数据库查询的图书
            for (Book book : bookList) {
                AiBookDTO dto = new AiBookDTO();
                dto.setBookName(book.getBookName());
                dto.setAuthor(book.getAuthor());
                dto.setCategory(book.getCategory());
                dto.setReason("结合您的阅读偏好，为您推荐图书");
                result.add(dto);
            }
        }
        
        return result;
    }

    // ========== 管理员统计分析方法 ==========

    /**
     * 按时间段统计借阅数据
     */
    public Map<String, Object> getBorrowStats(String startDate, String endDate) {
        Map<String, Object> result = new HashMap<>();
        
        BorrowRecordExample example = new BorrowRecordExample();
        BorrowRecordExample.Criteria criteria = example.createCriteria();
        
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        if (startDate != null && !startDate.isEmpty()) {
            try {
                criteria.andBorrowTimeGreaterThanOrEqualTo(sdf.parse(startDate));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        if (endDate != null && !endDate.isEmpty()) {
            try {
                criteria.andBorrowTimeLessThanOrEqualTo(sdf.parse(endDate));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        
        List<BorrowRecord> records = borrowRecordMapper.selectByExample(example);
        
        // 统计借阅总量
        result.put("totalBorrowCount", records.size());
        
        // 统计已归还数量
        long returnedCount = records.stream()
                .filter(r -> r.getReturnTime() != null)
                .count();
        result.put("returnedCount", returnedCount);
        
        // 统计未归还数量
        result.put("notReturnedCount", records.size() - returnedCount);
        
        // 统计逾期数量
        long overdueCount = records.stream()
                .filter(r -> r.getIsOverdue() != null && r.getIsOverdue() == 1)
                .count();
        result.put("overdueCount", overdueCount);
        
        // 按分类统计借阅数量
        Map<String, Integer> categoryBorrowCount = new HashMap<>();
        for (BorrowRecord record : records) {
            Book book = bookMapper.selectByPrimaryKey(record.getBookId());
            if (book != null && book.getCategory() != null) {
                categoryBorrowCount.put(book.getCategory(), 
                        categoryBorrowCount.getOrDefault(book.getCategory(), 0) + 1);
            }
        }
        result.put("categoryBorrowCount", categoryBorrowCount);
        
        return result;
    }

    /**
     * 获取热门图书排行
     */
    public List<Map<String, Object>> getHotBooks(Integer limit) {
        List<Map<String, Object>> result = new ArrayList<>();
        
        // 统计每本书的借阅次数
        Map<Integer, Integer> bookBorrowCount = new HashMap<>();
        BorrowRecordExample example = new BorrowRecordExample();
        List<BorrowRecord> records = borrowRecordMapper.selectByExample(example);
        
        for (BorrowRecord record : records) {
            bookBorrowCount.put(record.getBookId(), 
                    bookBorrowCount.getOrDefault(record.getBookId(), 0) + 1);
        }
        
        // 按借阅次数排序，取前N本
        List<Map.Entry<Integer, Integer>> sortedBooks = bookBorrowCount.entrySet().stream()
                .sorted((a, b) -> b.getValue().compareTo(a.getValue()))
                .limit(limit)
                .collect(Collectors.toList());
        
        // 获取图书详情
        for (Map.Entry<Integer, Integer> entry : sortedBooks) {
            Book book = bookMapper.selectByPrimaryKey(entry.getKey());
            if (book != null) {
                Map<String, Object> item = new HashMap<>();
                item.put("bookId", book.getBookId());
                item.put("bookName", book.getBookName());
                item.put("author", book.getAuthor());
                item.put("category", book.getCategory());
                item.put("borrowCount", entry.getValue());
                item.put("totalStock", book.getTotalStock());
                
                // 计算剩余库存
                BorrowRecordExample borrowExample = new BorrowRecordExample();
                borrowExample.createCriteria()
                        .andBookIdEqualTo(book.getBookId())
                        .andReturnTimeIsNull();
                int borrowedCount = borrowRecordMapper.selectByExample(borrowExample).size();
                item.put("remainStock", book.getTotalStock() - borrowedCount);
                
                result.add(item);
            }
        }
        
        // 如果没有借阅记录，返回库存最多的图书作为推荐进货参考
        if (result.isEmpty()) {
            BookExample bookExample = new BookExample();
            bookExample.setOrderByClause("total_stock DESC");
            List<Book> books = bookMapper.selectByExample(bookExample);
            for (int i = 0; i < Math.min(limit, books.size()); i++) {
                Book book = books.get(i);
                Map<String, Object> item = new HashMap<>();
                item.put("bookId", book.getBookId());
                item.put("bookName", book.getBookName());
                item.put("author", book.getAuthor());
                item.put("category", book.getCategory());
                item.put("borrowCount", 0);
                item.put("totalStock", book.getTotalStock());
                item.put("remainStock", book.getTotalStock());
                result.add(item);
            }
        }
        
        return result;
    }

    /**
     * 获取借阅趋势数据（按日期统计）
     */
    public List<Map<String, Object>> getBorrowTrend(String startDate, String endDate) {
        List<Map<String, Object>> result = new ArrayList<>();
        
        BorrowRecordExample example = new BorrowRecordExample();
        BorrowRecordExample.Criteria criteria = example.createCriteria();
        
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date start = null;
        Date end = null;
        
        if (startDate != null && !startDate.isEmpty()) {
            try {
                start = sdf.parse(startDate);
                criteria.andBorrowTimeGreaterThanOrEqualTo(start);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else {
            // 默认查询最近30天
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DAY_OF_MONTH, -30);
            start = cal.getTime();
            criteria.andBorrowTimeGreaterThanOrEqualTo(start);
        }
        
        if (endDate != null && !endDate.isEmpty()) {
            try {
                end = sdf.parse(endDate);
                criteria.andBorrowTimeLessThanOrEqualTo(end);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else {
            end = new Date();
        }
        
        List<BorrowRecord> records = borrowRecordMapper.selectByExample(example);
        
        // 按日期统计
        Map<String, Integer> dateCount = new TreeMap<>();
        
        // 初始化所有日期
        Calendar cal = Calendar.getInstance();
        cal.setTime(start);
        while (!cal.getTime().after(end)) {
            String dateStr = sdf.format(cal.getTime());
            dateCount.put(dateStr, 0);
            cal.add(Calendar.DAY_OF_MONTH, 1);
        }
        
        // 统计每天的借阅数量
        for (BorrowRecord record : records) {
            if (record.getBorrowTime() != null) {
                String dateStr = sdf.format(record.getBorrowTime());
                dateCount.put(dateStr, dateCount.getOrDefault(dateStr, 0) + 1);
            }
        }
        
        // 转换为列表格式
        for (Map.Entry<String, Integer> entry : dateCount.entrySet()) {
            Map<String, Object> item = new HashMap<>();
            item.put("date", entry.getKey());
            item.put("count", entry.getValue());
            result.add(item);
        }
        
        return result;
    }
}