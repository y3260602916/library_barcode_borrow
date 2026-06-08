package com.campus.libraryborrowbackend.entity;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class Book {
    private Integer bookId;
    private String bookBarcode;
    private String bookName;
    private String author;
    private String category;
    private Integer totalStock;
    private Integer remainStock;
    private BigDecimal price;
}