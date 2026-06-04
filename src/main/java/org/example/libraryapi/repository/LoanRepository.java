package org.example.libraryapi.repository;

import org.example.libraryapi.model.Loan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoanRepository extends JpaRepository<Loan, Long> {

    boolean existsByBookIdAndReturnDateIsNull(Long bookId);

    Page<Loan> findAll(Pageable pageable);
}