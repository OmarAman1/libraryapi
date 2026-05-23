package dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Request för att skapa en bok")
public class BookRequestDto {

    @NotBlank(message = "Title must not be blank")
    @Schema(example = "Harry Potter and the Philosopher's Stone")
    private String title;

    @NotBlank(message = "Author must not be blank")
    @Schema(example = "J.K. Rowling")
    private String author;

    @Schema(example = "9780747532743")
    private String isbn;

    @Schema(example = "1997")
    private int publishedYear;

    public BookRequestDto() {
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
