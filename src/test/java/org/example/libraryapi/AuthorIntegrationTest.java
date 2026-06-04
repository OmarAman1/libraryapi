package org.example.libraryapi;

import org.example.libraryapi.dto.AuthorDto;
import org.example.libraryapi.dto.BookRequestDto;
import org.example.libraryapi.dto.BookResponseDto;
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
class AuthorIntegrationTest {

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

    @Test
    void createAuthor_shouldReturn201AndSaveAuthor() {
        AuthorDto request = new AuthorDto();
        request.setName("Astrid Lindgren");

        ResponseEntity<AuthorDto> response =
                restTemplate.postForEntity(baseUrl() + "/authors", request, AuthorDto.class);

        assertEquals(201, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertNotNull(response.getBody().getId());
        assertEquals("Astrid Lindgren", response.getBody().getName());
        assertEquals(0, response.getBody().getBookCount());
        assertEquals(1, authorRepository.count());
    }

    @Test
    void getAuthorById_shouldReturn200AndAuthor() {
        AuthorDto request = new AuthorDto();
        request.setName("Selma Lagerlöf");

        ResponseEntity<AuthorDto> createResponse =
                restTemplate.postForEntity(baseUrl() + "/authors", request, AuthorDto.class);

        assertNotNull(createResponse.getBody());
        Long authorId = createResponse.getBody().getId();

        ResponseEntity<AuthorDto> response =
                restTemplate.getForEntity(baseUrl() + "/authors/" + authorId, AuthorDto.class);

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(authorId, response.getBody().getId());
        assertEquals("Selma Lagerlöf", response.getBody().getName());
        assertEquals(0, response.getBody().getBookCount());
    }

    @Test
    void getAuthorById_whenAuthorDoesNotExist_shouldReturn404() {
        ResponseEntity<String> response =
                restTemplate.getForEntity(baseUrl() + "/authors/9999", String.class);

        assertEquals(404, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().contains("Author with id 9999 not found"));
    }

    @Test
    void getBooksByAuthor_shouldReturnBooksForAuthorPage() {
        AuthorDto authorRequest = new AuthorDto();
        authorRequest.setName("Tove Jansson");

        ResponseEntity<AuthorDto> authorResponse =
                restTemplate.postForEntity(baseUrl() + "/authors", authorRequest, AuthorDto.class);

        assertNotNull(authorResponse.getBody());
        Long authorId = authorResponse.getBody().getId();

        BookRequestDto bookRequest = new BookRequestDto();
        bookRequest.setTitle("Muminpappans memoarer");
        bookRequest.setAuthorId(authorId);
        bookRequest.setIsbn("1234567890");
        bookRequest.setPublishedYear(1950);

        restTemplate.postForEntity(baseUrl() + "/books", bookRequest, BookResponseDto.class);

        ResponseEntity<String> response =
                restTemplate.getForEntity(baseUrl() + "/authors/" + authorId + "/books", String.class);

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().contains("\"content\""));
        assertTrue(response.getBody().contains("\"title\":\"Muminpappans memoarer\""));
        assertTrue(response.getBody().contains("\"authorName\":\"Tove Jansson\""));
        assertTrue(response.getBody().contains("\"totalElements\":1"));
    }

    @Test
    void getBooksByAuthor_whenAuthorHasNoBooks_shouldReturnEmptyPage() {
        AuthorDto authorRequest = new AuthorDto();
        authorRequest.setName("Vilhelm Moberg");

        ResponseEntity<AuthorDto> authorResponse =
                restTemplate.postForEntity(baseUrl() + "/authors", authorRequest, AuthorDto.class);

        assertNotNull(authorResponse.getBody());
        Long authorId = authorResponse.getBody().getId();

        ResponseEntity<String> response =
                restTemplate.getForEntity(baseUrl() + "/authors/" + authorId + "/books", String.class);

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().contains("\"content\":[]"));
        assertTrue(response.getBody().contains("\"totalElements\":0"));
    }

    @Test
    void createAuthor_whenNameIsBlank_shouldReturn400() {
        AuthorDto request = new AuthorDto();
        request.setName("");

        ResponseEntity<String> response =
                restTemplate.postForEntity(baseUrl() + "/authors", request, String.class);

        assertEquals(400, response.getStatusCode().value());
    }

    @Test
    void createAuthor_whenNameIsNull_shouldReturn400() {
        AuthorDto request = new AuthorDto();
        request.setName(null);

        ResponseEntity<String> response =
                restTemplate.postForEntity(baseUrl() + "/authors", request, String.class);

        assertEquals(400, response.getStatusCode().value());
    }

    @Test
    void getBooksByAuthor_whenAuthorDoesNotExist_shouldReturn404() {
        ResponseEntity<String> response =
                restTemplate.getForEntity(baseUrl() + "/authors/9999/books", String.class);

        assertEquals(404, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().contains("Author with id 9999 not found"));
    }

    @Test
    void createAuthor_withWhitespaceName_shouldReturn400() {
        AuthorDto request = new AuthorDto();
        request.setName("   ");

        ResponseEntity<String> response =
                restTemplate.postForEntity(baseUrl() + "/authors", request, String.class);

        assertEquals(400, response.getStatusCode().value());
    }
}