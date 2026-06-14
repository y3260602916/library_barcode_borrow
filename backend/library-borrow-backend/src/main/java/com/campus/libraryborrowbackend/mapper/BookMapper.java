package com.campus.libraryborrowbackend.mapper;

import com.campus.libraryborrowbackend.dto.AiBookDTO;
import com.campus.libraryborrowbackend.entity.Book;
import com.campus.libraryborrowbackend.entity.BookExample;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

public interface BookMapper {
    long countByExample(BookExample example);

    int deleteByExample(BookExample example);

    int deleteByPrimaryKey(Integer bookId);

    int insert(Book row);

    int insertSelective(Book row);

    List<Book> selectByExample(BookExample example);

    Book selectByPrimaryKey(Integer bookId);

    int updateByExampleSelective(@Param("row") Book row, @Param("example") BookExample example);

    int updateByExample(@Param("row") Book row, @Param("example") BookExample example);

    int updateByPrimaryKeySelective(Book row);

    int updateByPrimaryKey(Book row);

    Book selectBookByBarcode(@Param("bookBarcode") String bookBarcode);

    // ========= 新增方法 =========
    /**
     * 热门图书（借阅排行前N）
     */
    List<Book> selectHotBooks(@Param("limit") int limit);

    /**
     * 按分类推荐，排除当前用户已借未还图书
     */
    List<Book> selectRecommendBooks(
            @Param("categories") List<String> categories,
            @Param("userId") Integer userId,
            @Param("limit") int limit
    );

    List<Map<String, Object>> selectBookCategoryStats();

    /**
     * 冷启动兜底：获取热门图书（按借阅量排序，取前5本）
     */
    List<AiBookDTO> selectHotBooksForColdStart();


}