package org.example.libraryapi;

import org.example.libraryapi.model.Author;
import org.example.libraryapi.model.Book;
import org.example.libraryapi.model.Loan;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ModelTest {

    @Test
    void authorModel_shouldSetAndGetValues() {
        Author author = new Author();
        author.setId(1L);
        author.setName("Test Author");
        author.setBooks(new ArrayList<>());

        assertEquals(1L, author.getId());
        assertEquals("Test Author", author.getName());
        assertNotNull(author.getBooks());
        assertEquals(0, author.getBooks().size());
    }

    @Test
    void authorModel_shouldStoreBooksList() {
        Author author = new Author();
        Book book1 = new Book();
        Book book2 = new Book();

        List<Book> books = new ArrayList<>();
        books.add(book1);
        books.add(book2);

        author.setBooks(books);

        assertNotNull(author.getBooks());
        assertEquals(2, author.getBooks().size());
    }

    @Test
    void bookModel_shouldSetAndGetValues() {
        Author author = new Author();
        author.setId(2L);
        author.setName("Book Author");

        Book book = new Book();
        book.setId(10L);
        book.setTitle("Test Book");
        book.setAuthor(author);
        book.setIsbn("1234567890");
        book.setPublishedYear(2024);

        assertEquals(10L, book.getId());
        assertEquals("Test Book", book.getTitle());
        assertNotNull(book.getAuthor());
        assertEquals("Book Author", book.getAuthor().getName());
        assertEquals("1234567890", book.getIsbn());
        assertEquals(2024, book.getPublishedYear());
    }

    @Test
    void bookModel_shouldHandleNullAuthor() {
        Book book = new Book();
        book.setId(11L);
        book.setTitle("No Author Book");
        book.setAuthor(null);
        book.setIsbn(null);
        book.setPublishedYear(1999);

        assertEquals(11L, book.getId());
        assertEquals("No Author Book", book.getTitle());
        assertNull(book.getAuthor());
        assertNull(book.getIsbn());
        assertEquals(1999, book.getPublishedYear());
    }

    @Test
    void loanModel_shouldSetAndGetValues() {
        Author author = new Author();
        author.setId(3L);
        author.setName("Loan Author");

        Book book = new Book();
        book.setId(20L);
        book.setTitle("Loan Book");
        book.setAuthor(author);

        Loan loan = new Loan();
        loan.setId(100L);
        loan.setBook(book);
        loan.setLoanDate(LocalDate.of(2024, 1, 10));
        loan.setReturnDate(null);

        assertEquals(100L, loan.getId());
        assertNotNull(loan.getBook());
        assertEquals("Loan Book", loan.getBook().getTitle());
        assertEquals(LocalDate.of(2024, 1, 10), loan.getLoanDate());
        assertNull(loan.getReturnDate());
    }

    @Test
    void loanModel_shouldSetReturnDate() {
        Loan loan = new Loan();
        loan.setId(101L);
        loan.setLoanDate(LocalDate.of(2024, 3, 1));
        loan.setReturnDate(LocalDate.of(2024, 3, 15));

        assertEquals(101L, loan.getId());
        assertEquals(LocalDate.of(2024, 3, 1), loan.getLoanDate());
        assertEquals(LocalDate.of(2024, 3, 15), loan.getReturnDate());
    }
}