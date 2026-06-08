package com.campus.libraryborrowbackend.mapper;

import com.campus.libraryborrowbackend.entity.BorrowRecord;
import com.campus.libraryborrowbackend.entity.BorrowRecordExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface BorrowRecordMapper {
    long countByExample(BorrowRecordExample example);

    int deleteByExample(BorrowRecordExample example);

    int deleteByPrimaryKey(Integer recordId);

    int insert(BorrowRecord row);

    int insertSelective(BorrowRecord row);

    List<BorrowRecord> selectByExample(BorrowRecordExample example);

    BorrowRecord selectByPrimaryKey(Integer recordId);

    int updateByExampleSelective(@Param("row") BorrowRecord row, @Param("example") BorrowRecordExample example);

    int updateByExample(@Param("row") BorrowRecord row, @Param("example") BorrowRecordExample example);

    int updateByPrimaryKeySelective(BorrowRecord row);

    int updateByPrimaryKey(BorrowRecord row);
}