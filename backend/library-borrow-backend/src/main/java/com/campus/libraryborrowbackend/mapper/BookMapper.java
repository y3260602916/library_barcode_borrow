package com.campus.libraryborrowbackend.mapper;

import com.campus.libraryborrowbackend.entity.Book;
import com.campus.libraryborrowbackend.entity.BookExample;
import java.util.List;
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

}