package com.permovdb.permovdb;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.permovdb.permovdb.domain.Movie;
import com.permovdb.permovdb.domain.Root;
import com.permovdb.permovdb.domain.SearchEntry;
import com.permovdb.permovdb.domain.TvRoot;
import com.permovdb.permovdb.domain.TvShow;
import com.permovdb.permovdb.domain.User;
import com.permovdb.permovdb.service.MovieService;
import com.permovdb.permovdb.service.TvShowService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class Controller {
    @Autowired
    MovieService movieService;
    @Autowired
    TvShowService tvShowService;

    @GetMapping("/{mediaType}/{mediaId}")
    public ResponseEntity<?> getMovieById(
            @PathVariable(name = "mediaId", required = true) String mediaId,
            @PathVariable(name = "mediaType", required = true) String mediaType)
            throws IOException, InterruptedException {

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(
                        "https://api.themoviedb.org/3/"
                                + mediaType + "/"
                                + mediaId))
                .header("accept", "application/json")
                .header("Authorization",
                        "Bearer " + null)
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();

        HttpResponse<String> response = HttpClient.newHttpClient().send(request,
                HttpResponse.BodyHandlers.ofString());

        ObjectMapper mapper = new ObjectMapper();

        // db entry
        if (!response.body().isEmpty()) {
            if (mediaType.equals("movie")) {
                Movie movie = mapper.readValue(response.body(), Movie.class);
            } else {
                TvShow tvShow = mapper.readValue(response.body(), TvShow.class);
            }
        }
        return new ResponseEntity<>(response.body(), HttpStatus.OK);
    }

    @GetMapping("/api/{mediaType}")
    public List<?> getMovies(
            HttpServletRequest request,
            HttpServletResponse response,
            @PathVariable(name = "mediaType", required = true) String mediaType) {

        return (mediaType.equals("movie") ? movieService.getAllMovies() : tvShowService.getAllTvShows());
    }

    @GetMapping("/{mediaType}/search/{searchQuery}")
    public ResponseEntity<String> searchHandler(
            @PathVariable(name = "searchQuery", required = true) String searchQuery,
            @PathVariable(name = "mediaType", required = true) String mediaType,
            HttpServletRequest request) {

        String username = jwtUtil.extractUsernameFromRequest(request);

        User user = userService.loadByUserName(username);

        if (user != null) {
            user.getSearchDataWithDate().add(new SearchEntry(searchQuery, LocalDateTime.now()));
            userService.updateUser(user);
        }

        HttpRequest requestRedirect = HttpRequest.newBuilder()
                .uri(URI.create("https://api.themoviedb.org/3/search/{mediaType}?query="
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

            String result = "";

            switch (mediaType) {
                case "movie":
                    Root root = mapper.readValue(response.body(), Root.class);

                    try {
                        result = mapper.writeValueAsString(root.results);
                    } catch (Exception e) {
                        System.out.println("Error while creating movie database entry.");
                        System.out.println(e.getLocalizedMessage());
                    }

                    for (Movie movie : root.results) { // redirecting poster paths to the image url

                        movie.setPoster_path("https://image.tmdb.org/t/p/original" +
                                movie.getPoster_path());
                        movie.setBackdrop_path("https://image.tmdb.org/t/p/original" +
                                movie.getBackdrop_path());

                        movieService.saveMovie(movie);
                    }
                    break;
                case "tv":
                    TvRoot tvRoot = mapper.readValue(response.body(), TvRoot.class);

                    try {
                        result = mapper.writeValueAsString(tvRoot.results);
                    } catch (Exception e) {
                        System.out.println("Error while creating tv database entry.");
                        System.out.println(e.getLocalizedMessage());
                    }

                    for (TvShow tvShow : tvRoot.results) { // redirecting poster paths to the image
                                                           // url

                        tvShow.setPoster_path("https://image.tmdb.org/t/p/original" +
                                tvShow.getPoster_path());
                        tvShow.setBackdrop_path("https://image.tmdb.org/t/p/original" +
                                tvShow.getBackdrop_path());

                        tvShowService.saveTvShow(tvShow);
                    }
                    break;

                default:
                    break;
            }

            return new ResponseEntity<>(result, HttpStatus.OK);

        } catch (IOException | InterruptedException e) {
            System.out.println("Search call error");
            System.out.println(e.getLocalizedMessage());
            return new ResponseEntity<>(null, HttpStatus.EXPECTATION_FAILED);
        }

    }

}
