package com.campus.libraryborrowbackend.entity;

import lombok.Data;
import java.util.Date;

@Data
public class ScanRecord {
    private Integer scanId;
    private String imgPath;
    private String scanBarcode;
    private Date scanTime;
    private String result;
}