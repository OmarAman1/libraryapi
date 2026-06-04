package org.example.libraryapi.dto;

import java.util.List;

public class RecommendationResponseDto {

    private String location;
    private WeatherDto weather;
    private List<ActivityDto> activities;

    public RecommendationResponseDto() {
    }

    public RecommendationResponseDto(String location, WeatherDto weather, List<ActivityDto> activities) {
        this.location = location;
        this.weather = weather;
        this.activities = activities;
    }

    public String getLocation() {
        return location;
    }

    public WeatherDto getWeather() {
        return weather;
    }

    public List<ActivityDto> getActivities() {
        return activities;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setWeather(WeatherDto weather) {
        this.weather = weather;
    }

    public void setActivities(List<ActivityDto> activities) {
        this.activities = activities;
    }
}