package org.example.libraryapi.model;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
public class Loan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "book_id", unique = true, nullable = false)
    private Book book;

    @Column(nullable = false)
    private LocalDate loanDate;

    private LocalDate returnDate;

    public Loan() {
    }

    public Loan(Long id, Book book, LocalDate loanDate, LocalDate returnDate) {
        this.id = id;
        this.book = book;
        this.loanDate = loanDate;
        this.returnDate = returnDate;
    }

    public Long getId() {
        return id;
    }

    public Book getBook() {
        return book;
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

    public void setBook(Book book) {
        this.book = book;
    }

    public void setLoanDate(LocalDate loanDate) {
        this.loanDate = loanDate;
    }

    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }
}