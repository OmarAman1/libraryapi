package org.example.libraryapi.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class AuthorDto {

    private Long id;

    @NotBlank(message = "Author name must not be blank")
    @Size(min = 2, max = 100, message = "Author name must be between 2 and 100 characters")
    private String name;

    private int bookCount;

    public AuthorDto() {
    }

    public AuthorDto(Long id, String name, int bookCount) {
        this.id = id;
        this.name = name;
        this.bookCount = bookCount;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getBookCount() {
        return bookCount;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setBookCount(int bookCount) {
        this.bookCount = bookCount;
    }
}