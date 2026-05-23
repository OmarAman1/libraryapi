package dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "Wrapper response för API v2")
public class BookV2WrapperResponseDto {

    private List<BookV2ResponseDto> data;
    private String version;

    public BookV2WrapperResponseDto() {
    }

    public BookV2WrapperResponseDto(List<BookV2ResponseDto> data, String version) {
        this.data = data;
        this.version = version;
    }

    public List<BookV2ResponseDto> getData() {
        return data;
    }

    public void setData(List<BookV2ResponseDto> data) {
        this.data = data;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}
