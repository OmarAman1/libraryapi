package org.example.libraryapi.service;

import org.example.libraryapi.dto.WeatherDto;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Service
public class WeatherService {

    private final WebClient webClient;

    public WeatherService(WebClient webClient) {
        this.webClient = webClient;
    }

    public Mono<WeatherDto> getWeather(String location) {
        return webClient.get()
                .uri("https://httpbin.org/get?city=" + location + "&apiKey=test-key")
                .retrieve()
                .bodyToMono(String.class)
                .timeout(Duration.ofSeconds(3))
                .map(response -> new WeatherDto("Sunny", 20.0))
                .onErrorReturn(new WeatherDto("Sunny", 20.0));
    }
}