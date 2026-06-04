package org.example.libraryapi.service;

import org.example.libraryapi.dto.ActivityDto;
import org.example.libraryapi.dto.WeatherDto;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.List;

@Service
public class ActivityService {

    private final WebClient webClient;

    public ActivityService(WebClient webClient) {
        this.webClient = webClient;
    }

    public Mono<List<ActivityDto>> getActivities(String location, WeatherDto weather) {

        String activityType = chooseActivityType(weather.getCondition());

        return webClient.get()
                .uri("https://httpbin.org/get?city=" + location + "&activity=" + activityType)
                .header(HttpHeaders.AUTHORIZATION, "Bearer test-token")
                .retrieve()
                .bodyToMono(String.class)
                .timeout(Duration.ofSeconds(3))
                .map(response -> List.of(
                        new ActivityDto("Visit local museum", activityType),
                        new ActivityDto("Explore city center", activityType)
                ))
                .onErrorReturn(getFallbackActivities());
    }

    private String chooseActivityType(String condition) {

        if (condition.equalsIgnoreCase("Rainy")) {
            return "museum";
        }

        if (condition.equalsIgnoreCase("Sunny")) {
            return "park";
        }

        return "city";
    }

    private List<ActivityDto> getFallbackActivities() {
        return List.of(
                new ActivityDto("City walk", "default"),
                new ActivityDto("Visit local museum", "default")
        );
    }
}