# Smart Reseassistent

## Beskrivning

Detta projekt är en Spring Boot-applikation som ger aktivitetsrekommendationer baserat på väderinformation för en vald stad.

Systemet använder:

- Spring Boot
- Spring WebClient
- REST API
- DTO-klasser
- Fallback-hantering vid fel

---

## Endpoint

```http
GET /api/recommendations?location={city}
```

Exempel:

```http
GET /api/recommendations?location=Stockholm
```

---

## Exempelsvar

```json
{
  "location": "Stockholm",
  "weather": {
    "condition": "Sunny",
    "temperature": 20.0
  },
  "activities": [
    {
      "name": "City walk",
      "type": "default"
    },
    {
      "name": "Visit local museum",
      "type": "default"
    }
  ]
}
```

---

## Arkitektur

Projektet består av följande komponenter:

### Controller

- `RecommendationController`
    - Exponerar REST-endpointen `/api/recommendations`.

### Services

- `RecommendationService`
    - Samordnar anrop mellan väder- och aktivitetsservice.

- `WeatherService`
    - Hämtar väderinformation från ett externt API med WebClient.

- `ActivityService`
    - Hämtar aktivitetsrekommendationer baserat på aktuellt väder.

### DTO-klasser

- `WeatherDto`
- `ActivityDto`
- `RecommendationResponseDto`

DTO-klasser används för att överföra data mellan applikationen och klienten.

---

## Feltolerans

Applikationen använder fallback-strategier för att hantera fel vid externa API-anrop.

### Väder-Fallback

Om väder-API:et inte svarar returneras standardväder:

```text
Sunny
```

### Aktivitets-Fallback

Om aktivitets-API:et inte svarar returneras en standardlista med aktiviteter:

```json
[
  {
    "name": "City walk",
    "type": "default"
  },
  {
    "name": "Visit local museum",
    "type": "default"
  }
]
```

### Timeout

WebClient är konfigurerad med timeout för att undvika att applikationen väntar för länge på externa tjänster.

---

## Säkerhet

Projektet använder Spring Security med Basic Authentication.

```text
Username: admin
Password: admin123
```

---

## Starta projektet

### Förutsättningar

- Java 21
- Maven
- IntelliJ IDEA

### Kör applikationen

1. Klona projektet.
2. Öppna projektet i IntelliJ IDEA.
3. Starta `LibraryapiApplication`.
4. Testa endpointen via Postman eller webbläsare.

---

## Testning

Exempel på anrop:

```http
GET http://localhost:8080/api/recommendations?location=Stockholm
```

Om externa API:er inte svarar kommer fallback-data att returneras istället för att applikationen kraschar.

---

## Tekniker

- Java 21
- Spring Boot
- Spring Web
- Spring WebFlux
- Spring Security
- WebClient
- Maven