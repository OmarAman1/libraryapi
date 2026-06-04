package org.example.libraryapi.exception;

public class LoanNotFoundException extends RuntimeException {

    public LoanNotFoundException(Long id) {
        super("Loan with id " + id + " not found");
    }
}