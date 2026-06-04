package org.example.libraryapi.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.example.libraryapi.dto.BookRequestDto;
import org.example.libraryapi.dto.BookResponseDto;
import org.example.libraryapi.service.BookService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/books")
public class BookV1Controller {

    private final BookService bookService;

    public BookV1Controller(BookService bookService) {
        this.bookService = bookService;
    }

    @Operation(summary = "Skapa en ny bok")
    @ApiResponse(responseCode = "201", description = "Boken skapades")
    @ApiResponse(responseCode = "400", description = "Felaktig request")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookResponseDto createBook(@Valid @RequestBody BookRequestDto requestDto) {
        return bookService.createBook(requestDto);
    }

    @Operation(summary = "Hämta alla böcker i API v1 med pagination")
    @ApiResponse(responseCode = "200", description = "Sida med böcker")
    @GetMapping
    public Page<BookResponseDto> getAllBooks(Pageable pageable) {
        return bookService.getAllBooks(pageable);
    }

    @Operation(summary = "Hämta en bok via id i API v1")
    @ApiResponse(responseCode = "200", description = "Boken hittades")
    @ApiResponse(responseCode = "404", description = "Boken hittades inte")
    @GetMapping("/{id}")
    public BookResponseDto getBookById(@PathVariable Long id) {
        return bookService.getBookById(id);
    }
}