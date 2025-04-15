// package com.permovdb.permovdb;

// import lombok.Data;
// import org.springframework.boot.SpringApplication;
// import org.springframework.boot.autoconfigure.SpringBootApplication;
// import org.springframework.stereotype.Service;
// import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.RequestParam;
// import org.springframework.web.bind.annotation.RestController;
// import org.springframework.web.reactive.function.client.WebClient;

// import java.util.List;

// @Service
// public class MovieService {
// private final WebClient webClient;
// private final String API_KEY = "YOUR_API_KEY";

// public MovieService() {
// this.webClient = WebClient.builder()
// .baseUrl("https://api.themoviedb.org/3")
// .defaultHeader("Authorization", "Bearer " + API_KEY)
// .defaultHeader("accept", "application/json")
// .build();
// }

// public MovieDbResponse getPopularMovies(int page) {
// return webClient.get()
// .uri(uriBuilder -> uriBuilder
// .path("/discover/movie")
// .queryParam("include_adult", true)
// .queryParam("include_video", false)
// .queryParam("language", "en-US")
// .queryParam("page", page)
// .queryParam("sort_by", "popularity.desc")
// .build())
// .retrieve()
// .bodyToMono(MovieDbResponse.class)
// .block();
// }

// @Data
// class MovieDbResponse {
// private List<Movie> results;
// private int page;
// private int total_pages;
// private int total_results;
// }

// @Data
// class Movie {
// private int id;
// private String title;
// private String release_date;
// private String overview;
// private boolean adult;
// private String poster_path;
// private double vote_average;
// }
// }
