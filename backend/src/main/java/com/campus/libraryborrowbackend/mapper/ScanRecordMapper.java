package com.campus.libraryborrowbackend.mapper;

import com.campus.libraryborrowbackend.entity.ScanRecord;
import com.campus.libraryborrowbackend.entity.ScanRecordExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface ScanRecordMapper {
    long countByExample(ScanRecordExample example);

    int deleteByExample(ScanRecordExample example);

    int deleteByPrimaryKey(Integer scanId);

    int insert(ScanRecord row);

    int insertSelective(ScanRecord row);

    List<ScanRecord> selectByExample(ScanRecordExample example);

    ScanRecord selectByPrimaryKey(Integer scanId);

    int updateByExampleSelective(@Param("row") ScanRecord row, @Param("example") ScanRecordExample example);

    int updateByExample(@Param("row") ScanRecord row, @Param("example") ScanRecordExample example);

    int updateByPrimaryKeySelective(ScanRecord row);

    int updateByPrimaryKey(ScanRecord row);
}