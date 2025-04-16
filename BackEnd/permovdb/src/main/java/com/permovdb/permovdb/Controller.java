package com.permovdb.permovdb;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@RestController
class Controller {
        @Value("${api.key}")
        private String apiKey;

        @RestController
        class MovieController {
                @GetMapping("/")
                public String getMethodName() throws IOException, InterruptedException {
                        HttpRequest request = HttpRequest.newBuilder()
                                        .uri(URI.create(
                                                        "https://api.themoviedb.org/3/discover/movie?include_adult=true&include_video=false&language=en-US&page=1&sort_by=popularity.desc"))
                                        .header("accept", "application/json")
                                        .header("Authorization",
                                                        "Bearer " + apiKey)
                                        .method("GET", HttpRequest.BodyPublishers.noBody())
                                        .build();

                        HttpResponse<String> response = HttpClient.newHttpClient().send(request,
                                        HttpResponse.BodyHandlers.ofString());

                        ObjectMapper mapper = new ObjectMapper();

                        Root root = mapper.readValue(response.body(), Root.class);

                        for (Movie movie : root.results) { // redirecting poster paths to the image
                                movie.poster_path = "https://image.tmdb.org/t/p/original" +
                                                movie.poster_path;
                                movie.backdrop_path = "https://image.tmdb.org/t/p/original" +
                                                movie.backdrop_path;
                        }

                        // System.out.println(root.results.get(0).release_date);

                        // for (Movie movie : root.results) {
                        // // System.out.println(movie.original_title);
                        // }
                        // System.out.println(response.body());
                        return mapper.writeValueAsString(root.results);
                }

        }

}
