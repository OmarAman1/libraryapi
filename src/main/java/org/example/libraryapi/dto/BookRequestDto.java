package org.example.libraryapi.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Request för att skapa en bok")
public class BookRequestDto {

    @NotBlank(message = "Title must not be blank")
    @Schema(example = "Harry Potter and the Philosopher's Stone")
    private String title;

    @NotNull(message = "Author id must not be null")
    @Schema(example = "1")
    private Long authorId;

    @Schema(example = "9780747532743")
    private String isbn;

    @Schema(example = "1997")
    private int publishedYear;

    public BookRequestDto() {
    }

    public String getTitle() {
        return title;
    }

    public Long getAuthorId() {
        return authorId;
    }

    public String getIsbn() {
        return isbn;
    }

    public int getPublishedYear() {
        return publishedYear;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setAuthorId(Long authorId) {
        this.authorId = authorId;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public void setPublishedYear(int publishedYear) {
        this.publishedYear = publishedYear;
    }
}