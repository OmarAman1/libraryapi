package org.example.libraryapi;

import org.example.libraryapi.dto.AuthorDto;
import org.example.libraryapi.dto.BookRequestDto;
import org.example.libraryapi.dto.BookResponseDto;
import org.example.libraryapi.model.Author;
import org.example.libraryapi.model.Book;
import org.example.libraryapi.repository.AuthorRepository;
import org.example.libraryapi.repository.BookRepository;
import org.example.libraryapi.repository.LoanRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.TestRestTemplate;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureTestRestTemplate;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestRestTemplate
@ActiveProfiles("test")
public class BookIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private LoanRepository loanRepository;

    @BeforeEach
    void setUp() {
        restTemplate = restTemplate.withBasicAuth("admin", "admin123");

        loanRepository.deleteAll();
        bookRepository.deleteAll();
        authorRepository.deleteAll();
    }

    @Test
    void createBook_shouldReturn201AndSaveBook() {
        String authorUrl = "http://localhost:" + port + "/api/v1/authors";
        String bookUrl = "http://localhost:" + port + "/api/v1/books";

        AuthorDto authorRequest = new AuthorDto();
        authorRequest.setName("Robert C. Martin");

        ResponseEntity<AuthorDto> authorResponse =
                restTemplate.postForEntity(authorUrl, authorRequest, AuthorDto.class);

        assertEquals(HttpStatus.CREATED, authorResponse.getStatusCode());
        assertNotNull(authorResponse.getBody());
        assertNotNull(authorResponse.getBody().getId());

        BookRequestDto requestDto = new BookRequestDto();
        requestDto.setTitle("Clean Architecture");
        requestDto.setAuthorId(authorResponse.getBody().getId());
        requestDto.setIsbn("9780134494166");
        requestDto.setPublishedYear(2017);

        ResponseEntity<BookResponseDto> response =
                restTemplate.postForEntity(bookUrl, requestDto, BookResponseDto.class);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Clean Architecture", response.getBody().getTitle());
        assertEquals("Robert C. Martin", response.getBody().getAuthorName());
        assertEquals(1, bookRepository.count());
    }

    @Test
    void getBookById_shouldReturnBook() {
        Author author = new Author();
        author.setName("Eric Evans");
        Author savedAuthor = authorRepository.save(author);

        Book book = new Book();
        book.setTitle("Domain-Driven Design");
        book.setAuthor(savedAuthor);
        book.setIsbn("9780321125217");
        book.setPublishedYear(2003);

        Book savedBook = bookRepository.save(book);

        String url = "http://localhost:" + port + "/api/v1/books/" + savedBook.getId();

        ResponseEntity<BookResponseDto> response =
                restTemplate.getForEntity(url, BookResponseDto.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Domain-Driven Design", response.getBody().getTitle());
        assertEquals("Eric Evans", response.getBody().getAuthorName());
        assertEquals(savedAuthor.getId(), response.getBody().getAuthorId());
    }

    @Test
    void getBookById_whenBookDoesNotExist_shouldReturn404() {
        String url = "http://localhost:" + port + "/api/v1/books/9999";

        ResponseEntity<String> response =
                restTemplate.getForEntity(url, String.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().contains("Book with id 9999 not found"));
    }

    @Test
    void createBook_whenTitleIsBlank_shouldReturn400() {
        String authorUrl = "http://localhost:" + port + "/api/v1/authors";
        String bookUrl = "http://localhost:" + port + "/api/v1/books";

        AuthorDto authorRequest = new AuthorDto();
        authorRequest.setName("Test Author");

        ResponseEntity<AuthorDto> authorResponse =
                restTemplate.postForEntity(authorUrl, authorRequest, AuthorDto.class);

        assertNotNull(authorResponse.getBody());

        BookRequestDto requestDto = new BookRequestDto();
        requestDto.setTitle("");
        requestDto.setAuthorId(authorResponse.getBody().getId());
        requestDto.setIsbn("9780134494166");
        requestDto.setPublishedYear(2017);

        ResponseEntity<String> response =
                restTemplate.postForEntity(bookUrl, requestDto, String.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void createBook_whenAuthorDoesNotExist_shouldReturn404() {
        String url = "http://localhost:" + port + "/api/v1/books";

        BookRequestDto request = new BookRequestDto();
        request.setTitle("Test Book");
        request.setAuthorId(9999L);
        request.setIsbn("1234567890");
        request.setPublishedYear(2020);

        ResponseEntity<String> response =
                restTemplate.postForEntity(url, request, String.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().contains("Author with id 9999 not found"));
    }

    @Test
    void getBooks_whenNoBooksExist_shouldReturnEmptyPage() {
        String url = "http://localhost:" + port + "/api/v1/books";

        ResponseEntity<String> response =
                restTemplate.getForEntity(url, String.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().contains("\"content\":[]"));
        assertTrue(response.getBody().contains("\"totalElements\":0"));
    }

    @Test
    void createBook_whenTitleIsNull_shouldReturn400() {
        String authorUrl = "http://localhost:" + port + "/api/v1/authors";
        String bookUrl = "http://localhost:" + port + "/api/v1/books";

        AuthorDto authorRequest = new AuthorDto();
        authorRequest.setName("Valid Author");

        ResponseEntity<AuthorDto> authorResponse =
                restTemplate.postForEntity(authorUrl, authorRequest, AuthorDto.class);

        assertNotNull(authorResponse.getBody());

        BookRequestDto requestDto = new BookRequestDto();
        requestDto.setTitle(null);
        requestDto.setAuthorId(authorResponse.getBody().getId());
        requestDto.setIsbn("1234567890");
        requestDto.setPublishedYear(2024);

        ResponseEntity<String> response =
                restTemplate.postForEntity(bookUrl, requestDto, String.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
}