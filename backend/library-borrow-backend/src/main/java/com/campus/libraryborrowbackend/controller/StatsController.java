package com.campus.libraryborrowbackend.controller;

import com.campus.libraryborrowbackend.common.Result;
import com.campus.libraryborrowbackend.dto.AiBookDTO;
import com.campus.libraryborrowbackend.entity.Book;
import com.campus.libraryborrowbackend.service.StatsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/stats")
public class StatsController {

    @Autowired
    private StatsService statsService;

    @GetMapping("/overview")
    public Result<Map<String, Object>> getOverview(@RequestParam Integer userId) {
        return Result.success(statsService.getOverview(userId));
    }

    @GetMapping("/charts")
    public Result<Map<String, Object>> getCharts() {
        return Result.success(statsService.getCharts());
    }

    @GetMapping("/recommend")
    public Result<List<Book>> getRecommend(@RequestParam Integer userId) {
        return Result.success(statsService.getRecommendBooks(userId));
    }

    @GetMapping("/ai-recommend")
    public Result<List<AiBookDTO>> getAiRecommend(@RequestParam Integer userId) {
        return Result.success(statsService.getAiRecommend(userId));
    }

    // ========== 管理员统计分析接口 ==========

    /**
     * 按时间段统计借阅数据
     */
    @GetMapping("/borrow-stat")
    public Result<Map<String, Object>> getBorrowStats(
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate) {
        return Result.success(statsService.getBorrowStats(startDate, endDate));
    }

    /**
     * 获取热门图书排行
     */
    @GetMapping("/hot-books")
    public Result<List<Map<String, Object>>> getHotBooks(
            @RequestParam(defaultValue = "10") Integer limit) {
        return Result.success(statsService.getHotBooks(limit));
    }

    /**
     * 获取借阅趋势数据
     */
    @GetMapping("/borrow-trend")
    public Result<List<Map<String, Object>>> getBorrowTrend(
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate) {
        return Result.success(statsService.getBorrowTrend(startDate, endDate));
    }
}