package org.example.libraryapi;

import org.example.libraryapi.dto.AuthorDto;
import org.example.libraryapi.dto.BookRequestDto;
import org.example.libraryapi.dto.BookResponseDto;
import org.example.libraryapi.dto.LoanRequestDto;
import org.example.libraryapi.dto.LoanResponseDto;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class DtoTest {

    @Test
    void authorDto_shouldSetAndGetValues() {
        AuthorDto dto = new AuthorDto();
        dto.setId(1L);
        dto.setName("Astrid Lindgren");
        dto.setBookCount(3);

        assertEquals(1L, dto.getId());
        assertEquals("Astrid Lindgren", dto.getName());
        assertEquals(3, dto.getBookCount());
    }

    @Test
    void authorDto_shouldHandleNullValues() {
        AuthorDto dto = new AuthorDto();
        dto.setId(null);
        dto.setName(null);
        dto.setBookCount(0);

        assertNull(dto.getId());
        assertNull(dto.getName());
        assertEquals(0, dto.getBookCount());
    }

    @Test
    void bookRequestDto_shouldSetAndGetValues() {
        BookRequestDto dto = new BookRequestDto();
        dto.setTitle("Clean Code");
        dto.setAuthorId(2L);
        dto.setIsbn("9780132350884");
        dto.setPublishedYear(2008);

        assertEquals("Clean Code", dto.getTitle());
        assertEquals(2L, dto.getAuthorId());
        assertEquals("9780132350884", dto.getIsbn());
        assertEquals(2008, dto.getPublishedYear());
    }

    @Test
    void bookRequestDto_shouldHandleNullValues() {
        BookRequestDto dto = new BookRequestDto();
        dto.setTitle(null);
        dto.setAuthorId(null);
        dto.setIsbn(null);
        dto.setPublishedYear(0);

        assertNull(dto.getTitle());
        assertNull(dto.getAuthorId());
        assertNull(dto.getIsbn());
        assertEquals(0, dto.getPublishedYear());
    }

    @Test
    void bookResponseDto_shouldSetAndGetValues() {
        BookResponseDto dto = new BookResponseDto();
        dto.setId(10L);
        dto.setTitle("Domain-Driven Design");
        dto.setAuthorId(5L);
        dto.setAuthorName("Eric Evans");
        dto.setIsbn("9780321125217");
        dto.setPublishedYear(2003);

        assertEquals(10L, dto.getId());
        assertEquals("Domain-Driven Design", dto.getTitle());
        assertEquals(5L, dto.getAuthorId());
        assertEquals("Eric Evans", dto.getAuthorName());
        assertEquals("9780321125217", dto.getIsbn());
        assertEquals(2003, dto.getPublishedYear());
    }

    @Test
    void bookResponseDto_shouldHandleNullValues() {
        BookResponseDto dto = new BookResponseDto();
        dto.setId(null);
        dto.setTitle(null);
        dto.setAuthorId(null);
        dto.setAuthorName(null);
        dto.setIsbn(null);
        dto.setPublishedYear(0);

        assertNull(dto.getId());
        assertNull(dto.getTitle());
        assertNull(dto.getAuthorId());
        assertNull(dto.getAuthorName());
        assertNull(dto.getIsbn());
        assertEquals(0, dto.getPublishedYear());
    }

    @Test
    void loanRequestDto_shouldSetAndGetValues() {
        LoanRequestDto dto = new LoanRequestDto();
        dto.setBookId(7L);

        assertEquals(7L, dto.getBookId());
    }

    @Test
    void loanRequestDto_shouldHandleNullBookId() {
        LoanRequestDto dto = new LoanRequestDto();
        dto.setBookId(null);

        assertNull(dto.getBookId());
    }

    @Test
    void loanResponseDto_shouldSetAndGetValues() {
        LoanResponseDto dto = new LoanResponseDto();
        dto.setId(100L);
        dto.setBookId(20L);
        dto.setBookTitle("Refactoring");
        dto.setLoanDate(LocalDate.of(2024, 1, 10));
        dto.setReturnDate(LocalDate.of(2024, 1, 20));

        assertEquals(100L, dto.getId());
        assertEquals(20L, dto.getBookId());
        assertEquals("Refactoring", dto.getBookTitle());
        assertEquals(LocalDate.of(2024, 1, 10), dto.getLoanDate());
        assertEquals(LocalDate.of(2024, 1, 20), dto.getReturnDate());
    }

    @Test
    void loanResponseDto_shouldHandleNullReturnDate() {
        LoanResponseDto dto = new LoanResponseDto();
        dto.setId(200L);
        dto.setBookId(30L);
        dto.setBookTitle("Patterns of Enterprise Application Architecture");
        dto.setLoanDate(LocalDate.of(2024, 2, 1));
        dto.setReturnDate(null);

        assertEquals(200L, dto.getId());
        assertEquals(30L, dto.getBookId());
        assertEquals("Patterns of Enterprise Application Architecture", dto.getBookTitle());
        assertEquals(LocalDate.of(2024, 2, 1), dto.getLoanDate());
        assertNull(dto.getReturnDate());
    }
}