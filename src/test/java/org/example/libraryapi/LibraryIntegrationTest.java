package org.example.libraryapi;

import org.example.libraryapi.dto.AuthorDto;
import org.example.libraryapi.dto.BookRequestDto;
import org.example.libraryapi.dto.BookResponseDto;
import org.example.libraryapi.dto.LoanRequestDto;
import org.example.libraryapi.dto.LoanResponseDto;
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
public class LibraryIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private LoanRepository loanRepository;

    private String baseUrl() {
        return "http://localhost:" + port + "/api/v1";
    }

    @BeforeEach
    void setup() {
        restTemplate = restTemplate.withBasicAuth("admin", "admin123");

        loanRepository.deleteAll();
        bookRepository.deleteAll();
        authorRepository.deleteAll();
    }

    @Test
    void createAuthorAndBook_andFetchBooksByAuthor() {
        AuthorDto author = new AuthorDto();
        author.setName("Test Author");

        ResponseEntity<AuthorDto> authorResponse =
                restTemplate.postForEntity(baseUrl() + "/authors", author, AuthorDto.class);

        assertNotNull(authorResponse.getBody());
        Long authorId = authorResponse.getBody().getId();

        BookRequestDto book = new BookRequestDto();
        book.setTitle("Test Book");
        book.setAuthorId(authorId);
        book.setIsbn("1234567890");
        book.setPublishedYear(2024);

        restTemplate.postForEntity(baseUrl() + "/books", book, BookResponseDto.class);

        ResponseEntity<String> response =
                restTemplate.getForEntity(baseUrl() + "/authors/" + authorId + "/books", String.class);

        assertNotNull(response.getBody());
        assertTrue(response.getBody().contains("\"content\""));
        assertTrue(response.getBody().contains("\"title\":\"Test Book\""));
        assertTrue(response.getBody().contains("\"totalElements\":1"));
    }

    @Test
    void createLoan_andFetchLoans() {
        AuthorDto author = new AuthorDto();
        author.setName("Loan Author");

        ResponseEntity<AuthorDto> authorResponse =
                restTemplate.postForEntity(baseUrl() + "/authors", author, AuthorDto.class);

        assertNotNull(authorResponse.getBody());
        Long authorId = authorResponse.getBody().getId();

        BookRequestDto book = new BookRequestDto();
        book.setTitle("Loan Book");
        book.setAuthorId(authorId);
        book.setIsbn("1234567891");
        book.setPublishedYear(2024);

        ResponseEntity<BookResponseDto> bookResponse =
                restTemplate.postForEntity(baseUrl() + "/books", book, BookResponseDto.class);

        assertNotNull(bookResponse.getBody());
        Long bookId = bookResponse.getBody().getId();

        LoanRequestDto loan = new LoanRequestDto();
        loan.setBookId(bookId);

        ResponseEntity<LoanResponseDto> loanResponse =
                restTemplate.postForEntity(baseUrl() + "/loans", loan, LoanResponseDto.class);

        assertEquals(HttpStatus.CREATED, loanResponse.getStatusCode());

        ResponseEntity<String> response =
                restTemplate.getForEntity(baseUrl() + "/loans", String.class);

        assertNotNull(response.getBody());
        assertTrue(response.getBody().contains("\"content\""));
        assertTrue(response.getBody().contains("\"bookId\":" + bookId));
        assertTrue(response.getBody().contains("\"totalElements\":1"));
    }

    @Test
    void createLoan_onAlreadyLoanedBook_shouldReturn400() {
        AuthorDto author = new AuthorDto();
        author.setName("Error Author");

        ResponseEntity<AuthorDto> authorResponse =
                restTemplate.postForEntity(baseUrl() + "/authors", author, AuthorDto.class);

        assertNotNull(authorResponse.getBody());
        Long authorId = authorResponse.getBody().getId();

        BookRequestDto book = new BookRequestDto();
        book.setTitle("Error Book");
        book.setAuthorId(authorId);
        book.setIsbn("1234567892");
        book.setPublishedYear(2024);

        ResponseEntity<BookResponseDto> bookResponse =
                restTemplate.postForEntity(baseUrl() + "/books", book, BookResponseDto.class);

        assertNotNull(bookResponse.getBody());
        Long bookId = bookResponse.getBody().getId();

        LoanRequestDto loan = new LoanRequestDto();
        loan.setBookId(bookId);

        restTemplate.postForEntity(baseUrl() + "/loans", loan, LoanResponseDto.class);

        ResponseEntity<String> response =
                restTemplate.postForEntity(baseUrl() + "/loans", loan, String.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().contains("Book is already on loan"));
    }
}