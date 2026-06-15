package com.campus.libraryborrowbackend.dto;

import lombok.Data;

@Data
public class AiBookDTO {
    private Long bookId;
    private String bookName;
    private String author;
    private String category;
    private String reason;
}