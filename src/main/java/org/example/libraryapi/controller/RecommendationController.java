package org.example.libraryapi.controller;

import org.example.libraryapi.dto.RecommendationResponseDto;
import org.example.libraryapi.service.RecommendationService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Mono;

@RestController
public class RecommendationController {

    private final RecommendationService recommendationService;

    public RecommendationController(RecommendationService recommendationService) {
        this.recommendationService = recommendationService;
    }

    @GetMapping("/api/recommendations")
    public Mono<RecommendationResponseDto> getRecommendations(@RequestParam String location) {
        return recommendationService.getRecommendations(location);
    }
}