package com.permovdb.permovdb.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.permovdb.permovdb.domain.Movie;
import com.permovdb.permovdb.domain.Root;
import com.permovdb.permovdb.service.MovieService;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@RestController
class Controller {
        @Value("${api.key}")
        private String apiKey;

        @Autowired
        MovieService movieService;

        @RestController
        class MovieController {

                @GetMapping("/")
                public ResponseEntity<?> getMethodName() throws IOException, InterruptedException {
                        HttpRequest request = HttpRequest.newBuilder()
                                        .uri(URI.create(
                                                        "https://api.themoviedb.org/3/discover/movie?include_adult=false&include_video=false&language=en-US&page=1&sort_by=popularity.desc"))
                                        .header("accept", "application/json")
                                        .header("Authorization",
                                                        "Bearer " + apiKey)
                                        .method("GET", HttpRequest.BodyPublishers.noBody())
                                        .build();

                        HttpResponse<String> response = HttpClient.newHttpClient().send(request,
                                        HttpResponse.BodyHandlers.ofString());

                        ObjectMapper mapper = new ObjectMapper();

                        Root root = mapper.readValue(response.body(), Root.class);

                        for (Movie movie : root.results) { // redirecting poster paths to the image url

                                movie.setPoster_path("https://image.tmdb.org/t/p/original" +
                                                movie.getPoster_path());
                                movie.setBackdrop_path("https://image.tmdb.org/t/p/original" +
                                                movie.getBackdrop_path());

                                movie.setId(Long.valueOf(movie.getId()));

                                movieService.saveMovie(movie);
                        }

                        String result = mapper.writeValueAsString(root.results);
                        return new ResponseEntity<String>(result, HttpStatus.OK);
                }

        }

}
