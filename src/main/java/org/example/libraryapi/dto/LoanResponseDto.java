package org.example.libraryapi.dto;

import java.time.LocalDate;

public class LoanResponseDto {

    private Long id;
    private Long bookId;
    private String bookTitle;
    private LocalDate loanDate;
    private LocalDate returnDate;

    public LoanResponseDto() {
    }

    public LoanResponseDto(Long id, Long bookId, String bookTitle, LocalDate loanDate, LocalDate returnDate) {
        this.id = id;
        this.bookId = bookId;
        this.bookTitle = bookTitle;
        this.loanDate = loanDate;
        this.returnDate = returnDate;
    }

    public Long getId() {
        return id;
    }

    public Long getBookId() {
        return bookId;
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public LocalDate getLoanDate() {
        return loanDate;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }

    public void setBookTitle(String bookTitle) {
        this.bookTitle = bookTitle;
    }

    public void setLoanDate(LocalDate loanDate) {
        this.loanDate = loanDate;
    }

    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }
}