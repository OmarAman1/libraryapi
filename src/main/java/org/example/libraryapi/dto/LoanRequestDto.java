package org.example.libraryapi.dto;

import jakarta.validation.constraints.NotNull;

public class LoanRequestDto {

    @NotNull(message = "Book id must not be null")
    private Long bookId;

    public LoanRequestDto() {
    }

    public LoanRequestDto(Long bookId) {
        this.bookId = bookId;
    }

    public Long getBookId() {
        return bookId;
    }

    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }
}