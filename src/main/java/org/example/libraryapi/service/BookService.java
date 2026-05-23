package service;


import dto.BookRequestDto;
import dto.BookResponseDto;
import dto.BookV2ResponseDto;
import exception.BookNotFoundException;
import model.Book;
import org.springframework.stereotype.Service;
import repository.BookRepository;

import java.util.List;

@Service
public class BookService {

    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public BookResponseDto createBook(BookRequestDto requestDto) {
        Book book = new Book();
        book.setTitle(requestDto.getTitle());
        book.setAuthor(requestDto.getAuthor());
        book.setIsbn(requestDto.getIsbn());
        book.setPublishedYear(requestDto.getPublishedYear());

        Book savedBook = bookRepository.save(book);

        return mapToResponseDto(savedBook);
    }

    public List<BookResponseDto> getAllBooks() {
        return bookRepository.findAll()
                .stream()
                .map(this::mapToResponseDto)
                .toList();
    }

    public BookResponseDto getBookById(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException(id));

        return mapToResponseDto(book);
    }

    public List<BookV2ResponseDto> getAllBooksV2() {
        return bookRepository.findAll()
                .stream()
                .map(this::mapToV2ResponseDto)
                .toList();
    }

    private BookResponseDto mapToResponseDto(Book book) {
        return new BookResponseDto(
                book.getId(),
                book.getTitle(),
                book.getAuthor(),
                book.getIsbn(),
                book.getPublishedYear()
        );
    }

    private BookV2ResponseDto mapToV2ResponseDto(Book book) {
        return new BookV2ResponseDto(
                book.getId(),
                book.getTitle(),
                book.getAuthor(),
                book.getIsbn(),
                book.getPublishedYear(),
                true
        );
    }
}
