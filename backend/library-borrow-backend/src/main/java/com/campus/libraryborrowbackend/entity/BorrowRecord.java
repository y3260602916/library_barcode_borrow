package com.campus.libraryborrowbackend.entity;

import lombok.Data;
import java.util.Date;

@Data
public class BorrowRecord {
    private Integer recordId;
    private Integer userId;
    private Integer bookId;
    private Date borrowTime;
    private Date returnTime;
    private Integer isOverdue;
}