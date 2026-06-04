package org.example.libraryapi.service;

import jakarta.transaction.Transactional;
import org.example.libraryapi.dto.LoanRequestDto;
import org.example.libraryapi.dto.LoanResponseDto;
import org.example.libraryapi.exception.BookNotFoundException;
import org.example.libraryapi.model.Book;
import org.example.libraryapi.model.Loan;
import org.example.libraryapi.repository.BookRepository;
import org.example.libraryapi.repository.LoanRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class LoanService {

    private final LoanRepository loanRepository;
    private final BookRepository bookRepository;

    public LoanService(LoanRepository loanRepository, BookRepository bookRepository) {
        this.loanRepository = loanRepository;
        this.bookRepository = bookRepository;
    }

    @Transactional
    public synchronized LoanResponseDto createLoan(LoanRequestDto requestDto) {
        Book book = bookRepository.findById(requestDto.getBookId())
                .orElseThrow(() -> new BookNotFoundException(requestDto.getBookId()));

        boolean activeLoanExists = loanRepository.existsByBookIdAndReturnDateIsNull(book.getId());
        if (activeLoanExists) {
            throw new IllegalArgumentException("Book is already on loan");
        }

        Loan loan = new Loan();
        loan.setBook(book);
        loan.setLoanDate(LocalDate.now());
        loan.setReturnDate(null);

        Loan savedLoan = loanRepository.save(loan);
        return mapToResponseDto(savedLoan);
    }

    public Page<LoanResponseDto> getAllLoans(Pageable pageable) {

        return loanRepository.findAll(pageable)
                .map(this::mapToResponseDto);
    }

    private LoanResponseDto mapToResponseDto(Loan loan) {
        return new LoanResponseDto(
                loan.getId(),
                loan.getBook().getId(),
                loan.getBook().getTitle(),
                loan.getLoanDate(),
                loan.getReturnDate()
        );
    }
}