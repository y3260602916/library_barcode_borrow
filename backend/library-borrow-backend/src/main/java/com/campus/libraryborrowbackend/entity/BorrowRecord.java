package com.campus.libraryborrowbackend.entity;

import java.util.Date;

public class BorrowRecord {
    private Integer recordId;

    private Integer userId;

    private Integer bookId;

    private Date borrowTime;

    private Date returnTime;

    private Byte isOverdue;

    public Integer getRecordId() {
        return recordId;
    }

    public void setRecordId(Integer recordId) {
        this.recordId = recordId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getBookId() {
        return bookId;
    }

    public void setBookId(Integer bookId) {
        this.bookId = bookId;
    }

    public Date getBorrowTime() {
        return borrowTime;
    }

    public void setBorrowTime(Date borrowTime) {
        this.borrowTime = borrowTime;
    }

    public Date getReturnTime() {
        return returnTime;
    }

    public void setReturnTime(Date returnTime) {
        this.returnTime = returnTime;
    }

    public Byte getIsOverdue() {
        return isOverdue;
    }

    public void setIsOverdue(Byte isOverdue) {
        this.isOverdue = isOverdue;
    }
}