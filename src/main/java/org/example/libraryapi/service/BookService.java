package org.example.libraryapi.service;

import org.example.libraryapi.dto.BookRequestDto;
import org.example.libraryapi.dto.BookResponseDto;
import org.example.libraryapi.dto.BookV2ResponseDto;
import org.example.libraryapi.exception.AuthorNotFoundException;
import org.example.libraryapi.exception.BookNotFoundException;
import org.example.libraryapi.model.Author;
import org.example.libraryapi.model.Book;
import org.example.libraryapi.repository.AuthorRepository;
import org.example.libraryapi.repository.BookRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class BookService {

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;

    public BookService(BookRepository bookRepository, AuthorRepository authorRepository) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
    }

    @CacheEvict(value = "books", allEntries = true)
    public BookResponseDto createBook(BookRequestDto requestDto) {
        Author author = authorRepository.findById(requestDto.getAuthorId())
                .orElseThrow(() -> new AuthorNotFoundException(requestDto.getAuthorId()));

        Book book = new Book();
        book.setTitle(requestDto.getTitle());
        book.setAuthor(author);
        book.setIsbn(requestDto.getIsbn());
        book.setPublishedYear(requestDto.getPublishedYear());

        Book savedBook = bookRepository.save(book);

        return mapToResponseDto(savedBook);
    }

    public Page<BookResponseDto> getAllBooks(Pageable pageable) {
        return bookRepository.findAll(pageable)
                .map(this::mapToResponseDto);
    }

    @Cacheable(value = "books", key = "#id")
    public BookResponseDto getBookById(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException(id));

        return mapToResponseDto(book);
    }

    public Page<BookV2ResponseDto> getAllBooksV2(Pageable pageable) {
        return bookRepository.findAll(pageable)
                .map(this::mapToV2ResponseDto);
    }

    private BookResponseDto mapToResponseDto(Book book) {
        return new BookResponseDto(
                book.getId(),
                book.getTitle(),
                book.getAuthor() != null ? book.getAuthor().getName() : null,
                book.getAuthor() != null ? book.getAuthor().getId() : null,
                book.getIsbn(),
                book.getPublishedYear()
        );
    }

    private BookV2ResponseDto mapToV2ResponseDto(Book book) {
        return new BookV2ResponseDto(
                book.getId(),
                book.getTitle(),
                book.getAuthor() != null ? book.getAuthor().getName() : null,
                book.getIsbn(),
                book.getPublishedYear(),
                true
        );
    }
}