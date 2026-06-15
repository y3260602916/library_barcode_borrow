package com.campus.libraryborrowbackend.mapper;

import com.campus.libraryborrowbackend.entity.InventoryRecord;
import com.campus.libraryborrowbackend.entity.InventoryRecordExample;

import java.util.List;

public interface InventoryRecordMapper {
    int deleteByPrimaryKey(Integer recordId);

    int insert(InventoryRecord record);

    int insertSelective(InventoryRecord record);

    List<InventoryRecord> selectByExample(InventoryRecordExample example);

    InventoryRecord selectByPrimaryKey(Integer recordId);

    int updateByPrimaryKeySelective(InventoryRecord record);

    int updateByPrimaryKey(InventoryRecord record);

    /**
     * 查询所有出入库记录
     */
    List<InventoryRecord> selectAll();
}