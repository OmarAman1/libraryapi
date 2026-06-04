package org.example.libraryapi.repository;

import org.example.libraryapi.model.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {

    Page<Book> findAll(Pageable pageable);

    List<Book> findByAuthorId(Long authorId);
}