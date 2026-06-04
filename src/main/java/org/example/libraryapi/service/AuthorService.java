package org.example.libraryapi.service;

import org.example.libraryapi.dto.AuthorDto;
import org.example.libraryapi.dto.BookResponseDto;
import org.example.libraryapi.exception.AuthorNotFoundException;
import org.example.libraryapi.model.Author;
import org.example.libraryapi.model.Book;
import org.example.libraryapi.repository.AuthorRepository;
import org.example.libraryapi.repository.BookRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthorService {

    private final AuthorRepository authorRepository;
    private final BookRepository bookRepository;

    public AuthorService(AuthorRepository authorRepository,
                         BookRepository bookRepository) {
        this.authorRepository = authorRepository;
        this.bookRepository = bookRepository;
    }

    public AuthorDto createAuthor(AuthorDto dto) {

        Author author = new Author();
        author.setName(dto.getName());

        Author saved = authorRepository.save(author);

        return mapToDto(saved);
    }

    public AuthorDto getAuthorById(Long id) {

        Author author = authorRepository.findById(id)
                .orElseThrow(() -> new AuthorNotFoundException(id));

        return mapToDto(author);
    }

    public Page<BookResponseDto> getBooksByAuthor(Long authorId, Pageable pageable) {

        Author author = authorRepository.findById(authorId)
                .orElseThrow(() -> new AuthorNotFoundException(authorId));

        List<Book> books = bookRepository.findByAuthorId(author.getId());

        List<BookResponseDto> dtos = books.stream()
                .map(this::mapBookToDto)
                .toList();

        int start = (int) pageable.getOffset();

        if (start >= dtos.size()) {
            return new PageImpl<>(List.of(), pageable, dtos.size());
        }

        int end = Math.min(start + pageable.getPageSize(), dtos.size());

        List<BookResponseDto> pageContent = dtos.subList(start, end);

        return new PageImpl<>(pageContent, pageable, dtos.size());
    }

    private AuthorDto mapToDto(Author author) {

        int bookCount = author.getBooks() != null
                ? author.getBooks().size()
                : 0;

        return new AuthorDto(
                author.getId(),
                author.getName(),
                bookCount
        );
    }

    private BookResponseDto mapBookToDto(Book book) {

        BookResponseDto dto = new BookResponseDto();

        dto.setId(book.getId());
        dto.setTitle(book.getTitle());
        dto.setAuthorId(book.getAuthor().getId());
        dto.setAuthorName(book.getAuthor().getName());
        dto.setIsbn(book.getIsbn());
        dto.setPublishedYear(book.getPublishedYear());

        return dto;
    }
}