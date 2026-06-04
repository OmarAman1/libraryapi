package org.example.libraryapi.controller;

import jakarta.validation.Valid;
import org.example.libraryapi.dto.AuthorDto;
import org.example.libraryapi.dto.BookResponseDto;
import org.example.libraryapi.service.AuthorService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/authors")
public class AuthorController {

    private final AuthorService authorService;

    public AuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AuthorDto createAuthor(@Valid @RequestBody AuthorDto dto) {
        return authorService.createAuthor(dto);
    }

    @GetMapping("/{id}")
    public AuthorDto getAuthorById(@PathVariable Long id) {
        return authorService.getAuthorById(id);
    }

    @GetMapping("/{id}/books")
    public Page<BookResponseDto> getBooksByAuthor(
            @PathVariable Long id,
            Pageable pageable
    ) {
        return authorService.getBooksByAuthor(id, pageable);
    }
}