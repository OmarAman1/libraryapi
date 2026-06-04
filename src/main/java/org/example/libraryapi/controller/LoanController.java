package org.example.libraryapi.controller;

import jakarta.validation.Valid;
import org.example.libraryapi.dto.LoanRequestDto;
import org.example.libraryapi.dto.LoanResponseDto;
import org.example.libraryapi.service.LoanService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/loans")
public class LoanController {

    private final LoanService loanService;

    public LoanController(LoanService loanService) {
        this.loanService = loanService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public LoanResponseDto createLoan(@Valid @RequestBody LoanRequestDto requestDto) {
        return loanService.createLoan(requestDto);
    }

    @GetMapping
    public Page<LoanResponseDto> getAllLoans(Pageable pageable) {
        return loanService.getAllLoans(pageable);
    }
}