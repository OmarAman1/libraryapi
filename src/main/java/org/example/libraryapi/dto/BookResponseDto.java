package org.example.libraryapi.dto;

import java.io.Serializable;

public class BookResponseDto implements Serializable {

    private Long id;
    private String title;
    private String authorName;
    private Long authorId;
    private String isbn;
    private int publishedYear;

    public BookResponseDto() {
    }

    public BookResponseDto(
            Long id,
            String title,
            String authorName,
            Long authorId,
            String isbn,
            int publishedYear
    ) {
        this.id = id;
        this.title = title;
        this.authorName = authorName;
        this.authorId = authorId;
        this.isbn = isbn;
        this.publishedYear = publishedYear;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public Long getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Long authorId) {
        this.authorId = authorId;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public int getPublishedYear() {
        return publishedYear;
    }

    public void setPublishedYear(int publishedYear) {
        this.publishedYear = publishedYear;
    }
}