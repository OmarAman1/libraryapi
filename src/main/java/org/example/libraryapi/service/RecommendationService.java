package org.example.libraryapi.service;

import org.example.libraryapi.dto.RecommendationResponseDto;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class RecommendationService {

    private final WeatherService weatherService;
    private final ActivityService activityService;

    public RecommendationService(WeatherService weatherService, ActivityService activityService) {
        this.weatherService = weatherService;
        this.activityService = activityService;
    }

    public Mono<RecommendationResponseDto> getRecommendations(String location) {
        return weatherService.getWeather(location)
                .flatMap(weather ->
                        activityService.getActivities(location, weather)
                                .map(activities ->
                                        new RecommendationResponseDto(location, weather, activities)
                                )
                );
    }
}