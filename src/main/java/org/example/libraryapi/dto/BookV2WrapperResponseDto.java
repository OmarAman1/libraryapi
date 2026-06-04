package org.example.libraryapi.dto;

import org.springframework.data.domain.Page;

public class BookV2WrapperResponseDto {

    private Page<BookV2ResponseDto> data;
    private String version;

    public BookV2WrapperResponseDto() {
    }

    public BookV2WrapperResponseDto(Page<BookV2ResponseDto> data, String version) {
        this.data = data;
        this.version = version;
    }

    public Page<BookV2ResponseDto> getData() {
        return data;
    }

    public void setData(Page<BookV2ResponseDto> data) {
        this.data = data;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}