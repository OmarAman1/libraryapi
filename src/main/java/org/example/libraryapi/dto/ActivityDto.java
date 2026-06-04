package org.example.libraryapi.dto;

public class ActivityDto {

    private String name;
    private String type;

    public ActivityDto() {
    }

    public ActivityDto(String name, String type) {
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setType(String type) {
        this.type = type;
    }
}