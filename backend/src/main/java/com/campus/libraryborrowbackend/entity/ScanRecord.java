package com.campus.libraryborrowbackend.entity;

import java.util.Date;

public class ScanRecord {
    private Integer scanId;

    private String scanBarcode;

    private Date scanTime;

    private String result;

    public Integer getScanId() {
        return scanId;
    }

    public void setScanId(Integer scanId) {
        this.scanId = scanId;
    }

    public String getScanBarcode() {
        return scanBarcode;
    }

    public void setScanBarcode(String scanBarcode) {
        this.scanBarcode = scanBarcode == null ? null : scanBarcode.trim();
    }

    public Date getScanTime() {
        return scanTime;
    }

    public void setScanTime(Date scanTime) {
        this.scanTime = scanTime;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result == null ? null : result.trim();
    }
}