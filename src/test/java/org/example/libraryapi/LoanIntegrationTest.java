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
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestRestTemplate
@ActiveProfiles("test")
class LoanIntegrationTest {

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
    void setUp() {
        restTemplate = restTemplate.withBasicAuth("admin", "admin123");

        loanRepository.deleteAll();
        bookRepository.deleteAll();
        authorRepository.deleteAll();
    }

    private Long createAuthor(String name) {
        AuthorDto authorRequest = new AuthorDto();
        authorRequest.setName(name);

        ResponseEntity<AuthorDto> response =
                restTemplate.postForEntity(baseUrl() + "/authors", authorRequest, AuthorDto.class);

        assertNotNull(response.getBody());
        return response.getBody().getId();
    }

    private Long createBook(String title, Long authorId) {
        BookRequestDto bookRequest = new BookRequestDto();
        bookRequest.setTitle(title);
        bookRequest.setAuthorId(authorId);
        bookRequest.setIsbn("1111111111");
        bookRequest.setPublishedYear(2024);

        ResponseEntity<BookResponseDto> response =
                restTemplate.postForEntity(baseUrl() + "/books", bookRequest, BookResponseDto.class);

        assertNotNull(response.getBody());
        return response.getBody().getId();
    }

    @Test
    void createLoan_shouldReturn201AndSaveLoan() {
        Long authorId = createAuthor("Loan Author");
        Long bookId = createBook("Loan Book", authorId);

        LoanRequestDto request = new LoanRequestDto();
        request.setBookId(bookId);

        ResponseEntity<LoanResponseDto> response =
                restTemplate.postForEntity(baseUrl() + "/loans", request, LoanResponseDto.class);

        assertEquals(201, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertNotNull(response.getBody().getId());
        assertEquals(bookId, response.getBody().getBookId());
        assertEquals("Loan Book", response.getBody().getBookTitle());
        assertNull(response.getBody().getReturnDate());
        assertEquals(1, loanRepository.count());
    }

    @Test
    void getAllLoans_shouldReturnLoansPage() {
        Long authorId = createAuthor("Loan Author");
        Long bookId = createBook("Active Loan Book", authorId);

        LoanRequestDto request = new LoanRequestDto();
        request.setBookId(bookId);

        restTemplate.postForEntity(baseUrl() + "/loans", request, LoanResponseDto.class);

        ResponseEntity<String> response =
                restTemplate.getForEntity(baseUrl() + "/loans", String.class);

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().contains("\"content\""));
        assertTrue(response.getBody().contains("\"bookId\":" + bookId));
        assertTrue(response.getBody().contains("\"bookTitle\":\"Active Loan Book\""));
        assertTrue(response.getBody().contains("\"totalElements\":1"));
    }

    @Test
    void getAllLoans_whenNoLoansExist_shouldReturnEmptyPage() {
        ResponseEntity<String> response =
                restTemplate.getForEntity(baseUrl() + "/loans", String.class);

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().contains("\"content\":[]"));
        assertTrue(response.getBody().contains("\"totalElements\":0"));
    }

    @Test
    void createLoan_whenBookDoesNotExist_shouldReturn404() {
        LoanRequestDto request = new LoanRequestDto();
        request.setBookId(9999L);

        ResponseEntity<String> response =
                restTemplate.postForEntity(baseUrl() + "/loans", request, String.class);

        assertEquals(404, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().contains("Book with id 9999 not found"));
    }

    @Test
    void createLoan_whenBookIdIsNull_shouldReturn400() {
        LoanRequestDto request = new LoanRequestDto();
        request.setBookId(null);

        ResponseEntity<String> response =
                restTemplate.postForEntity(baseUrl() + "/loans", request, String.class);

        assertEquals(400, response.getStatusCode().value());
        assertNotNull(response.getBody());
    }

    @Test
    void createLoan_whenSameBookIsLoanedTwice_shouldReturn400() {
        Long authorId = createAuthor("Loan Author");
        Long bookId = createBook("Duplicated Loan Book", authorId);

        LoanRequestDto request = new LoanRequestDto();
        request.setBookId(bookId);

        ResponseEntity<LoanResponseDto> firstResponse =
                restTemplate.postForEntity(baseUrl() + "/loans", request, LoanResponseDto.class);

        ResponseEntity<String> secondResponse =
                restTemplate.postForEntity(baseUrl() + "/loans", request, String.class);

        assertEquals(201, firstResponse.getStatusCode().value());
        assertEquals(400, secondResponse.getStatusCode().value());
        assertNotNull(secondResponse.getBody());
        assertTrue(secondResponse.getBody().contains("Book is already on loan"));
        assertEquals(1, loanRepository.count());
    }

    @Test
    void createLoan_withEmptyBody_shouldReturn400() {
        ResponseEntity<String> response =
                restTemplate.postForEntity(baseUrl() + "/loans", null, String.class);

        assertEquals(400, response.getStatusCode().value());
    }
}