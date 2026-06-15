package com.campus.libraryborrowbackend.mapper;

import com.campus.libraryborrowbackend.entity.BorrowRecord;
import com.campus.libraryborrowbackend.entity.BorrowRecordExample;
import java.util.List;
import java.util.Map;
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

    // ========== 新增统计方法 ==========
    /**
     * 全馆已借出图书总数（未归还）
     */
    int selectBorrowedCount();

    /**
     * 当前用户未归还图书数量
     */
    int selectUserBorrowCount(@Param("userId") Integer userId);

    List<Map<String, Object>> selectWeeklyBorrowTrend();

    List<BorrowRecord> selectByUserId(@Param("userId") Integer userId);

    /**
     * 删除用户的所有借阅记录
     */
    int deleteByUserId(@Param("userId") Integer userId);
}