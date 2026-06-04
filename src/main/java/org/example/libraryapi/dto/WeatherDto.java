package org.example.libraryapi.dto;

public class WeatherDto {

    private String condition;
    private double temperature;

    public WeatherDto() {
    }

    public WeatherDto(String condition, double temperature) {
        this.condition = condition;
        this.temperature = temperature;
    }

    public String getCondition() {
        return condition;
    }

    public double getTemperature() {
        return temperature;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }
}