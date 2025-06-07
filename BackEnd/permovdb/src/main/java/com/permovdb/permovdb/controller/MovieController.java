package com.permovdb.permovdb.controller;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.permovdb.permovdb.domain.Cast;
import com.permovdb.permovdb.domain.CastMember;
import com.permovdb.permovdb.domain.Movie;
import com.permovdb.permovdb.domain.Root;
import com.permovdb.permovdb.domain.SearchEntry;
import com.permovdb.permovdb.domain.User;
import com.permovdb.permovdb.domain.DTO.MovieVideoDTO;
import com.permovdb.permovdb.domain.POJO.VideoMetadata;
import com.permovdb.permovdb.security.JwtUtil;
import com.permovdb.permovdb.service.CastMemberService;
import com.permovdb.permovdb.service.MovieService;
import com.permovdb.permovdb.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@RestController
class MovieController {
        LocalDate localDate = LocalDate.now().minusDays(1);

        @Value("${api.key}")
        private String apiKey;

        @Autowired
        MovieService movieService;

        @Autowired
        UserService userService;

        @Autowired
        private JwtUtil jwtUtil;

        @Autowired
        private CastMemberService castMemberService;

        @GetMapping("/")
        public ResponseEntity<?> getDiscoverData(
                        @RequestParam(value = "adult", defaultValue = "true") String adult,
                        @RequestParam(value = "releaseWindow", defaultValue = "") String releaseWindow,
                        @RequestParam(value = "vote_count", defaultValue = "0") String vote_count,
                        @RequestParam(value = "sort", defaultValue = "") String sort,
                        // @RequestParam(value = "compare", defaultValue = "popularity.desc") String
                        // compare,
                        @RequestParam(value = "page", defaultValue = "1") String page)
                        throws IOException, InterruptedException {

                System.out.println("new page request. page: " + page + " adult: " + adult);

                HttpRequest request = HttpRequest.newBuilder()
                                .uri(URI.create(
                                                "https://api.themoviedb.org/3/discover/movie?include_adult=true"
                                                                + "&include_video=false&with_original_language=en&page="
                                                                + page + "&sort_by=" + sort
                                                                + "&vote_count.gte=" + vote_count))
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

                        if (movie.getPoster_path() == null) {
                                System.out.println("nullll" + movie.getPoster_path());

                        } else {
                                movie.setPoster_path("https://image.tmdb.org/t/p/original" +
                                                movie.getPoster_path());
                                movie.setBackdrop_path("https://image.tmdb.org/t/p/original" +
                                                movie.getBackdrop_path());
                        }

                        movieService.saveMovie(movie);
                }
                // switch (compare) {
                // case "release":
                // root.results.sort(Comparator.comparing(Movie::getRelease_date).reversed());
                // break;
                // case "popularity":
                // root.results.sort(Comparator.comparing(Movie::getPopularity).reversed());
                // break;
                // case "vote":
                // root.results.sort(Comparator.comparing(Movie::getVote_average).reversed());
                // break;
                // default:
                // root.results.sort(Comparator.comparing(Movie::getPopularity).reversed());
                // break;
                // }

                String result = mapper.writeValueAsString(root.results);
                return new ResponseEntity<>(result, HttpStatus.OK);
        }

        @GetMapping("/movie/{movieId}/video")
        public ResponseEntity<String> getVideo(@PathVariable(name = "movieId", required = true) String movieId) {
                HttpRequest videoRequest = HttpRequest.newBuilder()
                                .uri(URI.create(
                                                "https://api.themoviedb.org/3/movie/" + movieId + "/credits"))
                                .header("accept", "application/json")
                                .header("Authorization",
                                                "Bearer " + apiKey)
                                .method("GET", HttpRequest.BodyPublishers.noBody())
                                .build();

                try {
                        HttpResponse<String> videoResponse = HttpClient.newHttpClient().send(videoRequest,
                                        HttpResponse.BodyHandlers.ofString());

                        ObjectMapper mapper = new ObjectMapper();

                        MovieVideoDTO movieVideoDTO = mapper.readValue(videoResponse.body(), MovieVideoDTO.class);

                        String trailer = "https://www.youtube.com/watch?v=";

                        for (VideoMetadata video : movieVideoDTO.getResults()) {
                                if (video.getType().toLowerCase().equals("trailer") &&
                                                video.getSite().toLowerCase().equals("youtube")) {
                                        trailer = trailer + video.getKey();
                                        break;
                                }
                        }

                        Movie movie = movieService.findMovieById(movieVideoDTO.getId());
                        if (movie != null && movie.getTrailer_path() == null) {
                                movie.setTrailer_path(trailer);
                                movieService.saveMovie(movie);
                        }

                        return new ResponseEntity<>(trailer, HttpStatus.OK);

                } catch (IOException e) {
                        e.printStackTrace();
                } catch (InterruptedException e) {
                        e.printStackTrace();
                }
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        @GetMapping("/movie/{movieId}/credits")
        public ResponseEntity<String> getCredits(@PathVariable(name = "movieId") String movieId) {
                HttpRequest castRequest = HttpRequest.newBuilder()
                                .uri(URI.create(
                                                "https://api.themoviedb.org/3/movie/" + movieId + "/credits"))
                                .header("accept", "application/json")
                                .header("Authorization",
                                                "Bearer " + apiKey)
                                .method("GET", HttpRequest.BodyPublishers.noBody())
                                .build();

                try {
                        HttpResponse<String> castResponse = HttpClient.newHttpClient().send(castRequest,
                                        HttpResponse.BodyHandlers.ofString());

                        ObjectMapper mapper = new ObjectMapper();

                        Cast cast = mapper.readValue(castResponse.body(), Cast.class);

                        return new ResponseEntity<>(castResponse.body(), HttpStatus.OK);

                } catch (IOException e) {
                        e.printStackTrace();
                } catch (InterruptedException e) {
                        e.printStackTrace();
                }
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

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

                // db entry
                if (!response.body().isEmpty()) {
                        Movie movie = mapper.readValue(response.body(), Movie.class);
                        System.out.println("Movie added to db: " + movie.getTitle() + "/"
                                        + movie.getRelease_date());

                }
                return new ResponseEntity<>(response.body(), HttpStatus.OK);
        }

        @GetMapping("/api/movies")
        public List<Movie> getMovies(HttpServletRequest request, HttpServletResponse response) {
                return movieService.getAllMovies();
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
