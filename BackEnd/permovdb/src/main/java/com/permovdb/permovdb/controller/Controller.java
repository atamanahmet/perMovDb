package com.permovdb.permovdb.controller;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import java.time.LocalDateTime;
// import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.permovdb.permovdb.domain.Movie;
import com.permovdb.permovdb.domain.Root;
import com.permovdb.permovdb.domain.SearchEntry;
import com.permovdb.permovdb.domain.User;
import com.permovdb.permovdb.security.JwtUtil;
import com.permovdb.permovdb.service.MovieService;
import com.permovdb.permovdb.service.UserService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
class Controller {
        @Value("${api.key}")
        private String apiKey;

        @Autowired
        MovieService movieService;

        @Autowired
        UserService userService;

        @Autowired
        private JwtUtil jwtUtil;

        @RestController
        class MovieController {

                @GetMapping("/")
                public ResponseEntity<?> getDicoverData() throws IOException, InterruptedException {
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

                        // System.out.println(response.body());

                        ObjectMapper mapper = new ObjectMapper();

                        Root root = mapper.readValue(response.body(), Root.class);

                        for (Movie movie : root.results) { // redirecting poster paths to the image url

                                movie.setPoster_path("https://image.tmdb.org/t/p/original" +
                                                movie.getPoster_path());
                                movie.setBackdrop_path("https://image.tmdb.org/t/p/original" +
                                                movie.getBackdrop_path());

                                movieService.saveMovie(movie);
                        }

                        String result = mapper.writeValueAsString(root.results);
                        return new ResponseEntity<>(result, HttpStatus.OK);
                }

                @GetMapping("/movie/{movieId}")
                public ResponseEntity<?> getMovieById(@PathVariable(name = "movieId", required = true) String movieId)
                                throws IOException, InterruptedException {

                        HttpRequest request = HttpRequest.newBuilder()
                                        .uri(URI.create(
                                                        "https://api.themoviedb.org/3/movie/"
                                                                        + movieId))
                                        .header("accept", "application/json")
                                        .header("Authorization",
                                                        "Bearer " + apiKey)
                                        .method("GET", HttpRequest.BodyPublishers.noBody())
                                        .build();

                        HttpResponse<String> response = HttpClient.newHttpClient().send(request,
                                        HttpResponse.BodyHandlers.ofString());

                        ObjectMapper mapper = new ObjectMapper();

                        // For db entry
                        if (!response.body().isEmpty()) {
                                Movie movie = mapper.readValue(response.body(), Movie.class);
                                System.out.println("Movie added to db: " + movie.getTitle() + "/"
                                                + movie.getRelease_date());

                        }
                        return new ResponseEntity<>(response.body(), HttpStatus.OK);
                }

                @GetMapping("/search/{searchQuery}")
                public ResponseEntity<String> searchHandler(
                                @PathVariable(name = "searchQuery", required = true) String searchQuery,
                                HttpServletRequest request) {

                        String username = jwtUtil.extractUsernameFromRequest(request);

                        User user = userService.loadByUserName(username);

                        if (user != null) {
                                user.getSearchDataWithDate().add(new SearchEntry(searchQuery, LocalDateTime.now()));
                                userService.updateUser(user);
                        }

                        HttpRequest requestRedirect = HttpRequest.newBuilder()
                                        .uri(URI.create("https://api.themoviedb.org/3/search/movie?query="
                                                        + searchQuery))
                                        .header("accept", "application/json")
                                        .header("Authorization",
                                                        "Bearer " + apiKey)
                                        .method("GET", HttpRequest.BodyPublishers.noBody())
                                        .build();

                        try {
                                HttpResponse<String> response = HttpClient.newHttpClient().send(requestRedirect,
                                                HttpResponse.BodyHandlers.ofString());

                                ObjectMapper mapper = new ObjectMapper();

                                Root root = mapper.readValue(response.body(), Root.class);

                                for (Movie movie : root.results) { // redirecting poster paths to the image url

                                        movie.setPoster_path("https://image.tmdb.org/t/p/original" +
                                                        movie.getPoster_path());
                                        movie.setBackdrop_path("https://image.tmdb.org/t/p/original" +
                                                        movie.getBackdrop_path());

                                        movieService.saveMovie(movie);
                                }

                                String result = mapper.writeValueAsString(root.results);

                                return new ResponseEntity<>(result, HttpStatus.OK);

                        } catch (IOException | InterruptedException e) {
                                System.out.println("Search call error");
                                System.out.println(e.getLocalizedMessage());
                                return new ResponseEntity<>(null, HttpStatus.EXPECTATION_FAILED);
                        }

                }

        }

}
