package dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Response för en bok i API v1")
public class BookResponseDto {

    @Schema(example = "1")
    private Long id;

    @Schema(example = "Harry Potter and the Philosopher's Stone")
    private String title;

    @Schema(example = "J.K. Rowling")
    private String author;

    @Schema(example = "9780747532743")
    private String isbn;

    @Schema(example = "1997")
    private int publishedYear;

    public BookResponseDto() {
    }

    public BookResponseDto(Long id, String title, String author, String isbn, int publishedYear) {
        this.id = id;
        this.title = title;
        this.author = author;
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

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
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
