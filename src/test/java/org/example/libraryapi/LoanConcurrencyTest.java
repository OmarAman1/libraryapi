package org.example.libraryapi;

import org.example.libraryapi.dto.AuthorDto;
import org.example.libraryapi.dto.BookRequestDto;
import org.example.libraryapi.dto.BookResponseDto;
import org.example.libraryapi.dto.LoanRequestDto;
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

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestRestTemplate
@ActiveProfiles("test")
class LoanConcurrencyTest {

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
    void oneHundredThreadsTryingToLoanSameBook_shouldCreateExactlyOneLoan() throws Exception {
        AuthorDto author = new AuthorDto();
        author.setName("Concurrency Author");

        ResponseEntity<AuthorDto> authorResponse =
                restTemplate.postForEntity(baseUrl() + "/authors", author, AuthorDto.class);

        Long authorId = authorResponse.getBody().getId();

        BookRequestDto book = new BookRequestDto();
        book.setTitle("Concurrency Book");
        book.setAuthorId(authorId);
        book.setIsbn("9999999999");
        book.setPublishedYear(2024);

        ResponseEntity<BookResponseDto> bookResponse =
                restTemplate.postForEntity(baseUrl() + "/books", book, BookResponseDto.class);

        Long bookId = bookResponse.getBody().getId();

        int numberOfThreads = 100;

        ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);
        CountDownLatch readyLatch = new CountDownLatch(numberOfThreads);
        CountDownLatch startLatch = new CountDownLatch(1);

        List<Integer> statusCodes = new CopyOnWriteArrayList<>();

        Runnable task = () -> {
            try {
                readyLatch.countDown();
                startLatch.await();

                LoanRequestDto loanRequest = new LoanRequestDto();
                loanRequest.setBookId(bookId);

                ResponseEntity<String> response =
                        restTemplate.postForEntity(baseUrl() + "/loans", loanRequest, String.class);

                statusCodes.add(response.getStatusCode().value());

            } catch (Exception e) {
                statusCodes.add(500);
            }
        };

        for (int i = 0; i < numberOfThreads; i++) {
            executorService.submit(task);
        }

        readyLatch.await();
        startLatch.countDown();

        executorService.shutdown();
        executorService.awaitTermination(20, TimeUnit.SECONDS);

        assertEquals(1, loanRepository.count());
        assertEquals(numberOfThreads, statusCodes.size());

        int successCount = 0;
        int badRequestCount = 0;

        for (Integer statusCode : statusCodes) {
            if (statusCode == 201) {
                successCount++;
            } else if (statusCode == 400) {
                badRequestCount++;
            }
        }

        assertEquals(1, successCount);
        assertEquals(99, badRequestCount);
    }
}