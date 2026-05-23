package controller;

import dto.BookV2WrapperResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import service.BookService;

@RestController
@RequestMapping("/api/v2/books")
public class BookV2Controller {

    private final BookService bookService;

    public BookV2Controller(BookService bookService) {
        this.bookService = bookService;
    }

    @Operation(summary = "Hämta alla böcker i API v2")
    @ApiResponse(responseCode = "200", description = "Lista med böcker i wrapper-format")
    @GetMapping
    public BookV2WrapperResponseDto getAllBooksV2() {
        return new BookV2WrapperResponseDto(bookService.getAllBooksV2(), "v2");
    }
}
