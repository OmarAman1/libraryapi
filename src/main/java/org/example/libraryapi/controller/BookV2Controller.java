package org.example.libraryapi.controller;

import io.swagger.v3.oas.annotations.Operation;
import org.example.libraryapi.dto.BookV2ResponseDto;
import org.example.libraryapi.dto.BookV2WrapperResponseDto;
import org.example.libraryapi.service.BookService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v2/books")
public class BookV2Controller {

    private final BookService bookService;

    public BookV2Controller(BookService bookService) {
        this.bookService = bookService;
    }

    @Operation(summary = "Hämta alla böcker i API v2 med pagination")
    @GetMapping
    public BookV2WrapperResponseDto getAllBooks(Pageable pageable) {
        Page<BookV2ResponseDto> books = bookService.getAllBooksV2(pageable);

        return new BookV2WrapperResponseDto(books, "v2");
    }
}