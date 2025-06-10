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
import java.util.List;
import java.util.stream.Collectors;

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
import com.permovdb.permovdb.domain.TvRoot;
import com.permovdb.permovdb.domain.TvShow;
import com.permovdb.permovdb.domain.User;
import com.permovdb.permovdb.domain.DTO.MovieVideoDTO;
import com.permovdb.permovdb.domain.POJO.VideoMetadata;
import com.permovdb.permovdb.security.JwtUtil;
// import com.permovdb.permovdb.service.CastMemberService;
import com.permovdb.permovdb.service.MovieService;
import com.permovdb.permovdb.service.TvShowService;
import com.permovdb.permovdb.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@RestController
class MovieController {
        LocalDate localDate = LocalDate.now().minusDays(1);

        private final String movieUrl = "https://api.themoviedb.org/3/discover/movie?";
        private final String tvUrl = "https://api.themoviedb.org/3/discover/tv?";

        @Value("${api.key}")
        private String apiKey;

        @Autowired
        MovieService movieService;
        @Autowired
        TvShowService tvShowService;

        @Autowired
        UserService userService;

        @Autowired
        private JwtUtil jwtUtil;

        // @Autowired
        // private CastMemberService castMemberService;

        @GetMapping("/")
        public ResponseEntity<?> getDiscoverData(
                        @RequestParam(value = "adult", defaultValue = "true") String adult,
                        @RequestParam(value = "releaseWindow", defaultValue = "") String releaseWindow,
                        @RequestParam(value = "vote_count", defaultValue = "500") String vote_count,
                        @RequestParam(value = "sort", defaultValue = "popularity.desc") String sort,
                        @RequestParam(value = "page", defaultValue = "1") String page)
                        throws IOException, InterruptedException {

                System.out.println("new page request. page: " + page + " adult: " + adult);

                HttpRequest request = HttpRequest.newBuilder()
                                .uri(URI.create(movieUrl + "include_adult=true"
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

                String result = mapper.writeValueAsString(root.results);
                return new ResponseEntity<>(result, HttpStatus.OK);
        }

        @GetMapping("/tv")
        public ResponseEntity<String> getTv(@RequestParam(value = "adult", defaultValue = "true") String adult,
                        @RequestParam(value = "releaseWindow", defaultValue = "") String releaseWindow,
                        @RequestParam(value = "vote_count", defaultValue = "500") String vote_count,
                        @RequestParam(value = "sort", defaultValue = "popularity.desc") String sort,
                        @RequestParam(value = "page", defaultValue = "1") String page)
                        throws IOException, InterruptedException {

                System.out.println("new TV page request. page: " + page + " adult: " + adult);

                HttpRequest request = HttpRequest.newBuilder()
                                .uri(URI.create(tvUrl + "include_adult=true"
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

                TvRoot tvRoot = mapper.readValue(response.body(), TvRoot.class);

                for (TvShow tvShow : tvRoot.results) { // redirecting poster paths to the image url

                        if (tvShow.getPoster_path() == null) {
                                System.out.println("nullll" + tvShow.getPoster_path());

                        } else {
                                tvShow.setPoster_path("https://image.tmdb.org/t/p/original" +
                                                tvShow.getPoster_path());
                                tvShow.setBackdrop_path("https://image.tmdb.org/t/p/original" +
                                                tvShow.getBackdrop_path());
                        }

                        tvShowService.saveTvShow(tvShow);
                }

                String result = mapper.writeValueAsString(tvRoot.results);
                return new ResponseEntity<>(result, HttpStatus.OK);
        }

        @GetMapping("/{mediaType}/{mediaId}/video")
        public ResponseEntity<String> getVideo(@PathVariable(name = "mediaType") String mediaType,
                        @PathVariable(name = "mediaId", required = true) String mediaId) {
                HttpRequest videoRequest = HttpRequest.newBuilder()
                                .uri(URI.create(
                                                "https://api.themoviedb.org/3/" + mediaType + "/" + mediaId
                                                                + "/videos"))
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

                        boolean videoExist = false;

                        for (VideoMetadata video : movieVideoDTO.getResults()) {
                                if (video.getType().toLowerCase().equals("trailer") &&
                                                video.getSite().toLowerCase().equals("youtube")) {
                                        trailer = trailer + video.getKey();
                                        videoExist = true;
                                        break;
                                }
                        }
                        if (!videoExist) {
                                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
                        }
                        if (mediaType.equals("movie")) {
                                Movie movie = movieService.findMovieById(movieVideoDTO.getId());
                                if (movie != null && movie.getTrailer_path() == null) {
                                        movie.setTrailer_path(trailer);
                                        movieService.saveMovie(movie);
                                }
                        } else if (mediaType.equals("tv")) {
                                TvShow tvShow = tvShowService.findTvShowById(movieVideoDTO.getId());
                                if (tvShow != null && tvShow.getTrailer_path() == null) {
                                        tvShow.setTrailer_path(trailer);
                                        tvShowService.saveTvShow(tvShow);
                                }
                        }

                        return new ResponseEntity<>(trailer, HttpStatus.OK);

                } catch (IOException e) {
                        e.printStackTrace();
                } catch (InterruptedException e) {
                        e.printStackTrace();
                }
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        @GetMapping("/{mediaType}/{movieId}/credits")
        public ResponseEntity<String> getCredits(
                        @PathVariable(name = "mediaType") String mediaType,
                        @PathVariable(name = "movieId") String movieId) {
                HttpRequest castRequest = HttpRequest.newBuilder()
                                .uri(URI.create(
                                                "https://api.themoviedb.org/3/" + mediaType + "/" + movieId
                                                                + "/credits"))
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

                        for (CastMember member : cast.getCast()) {
                                member.setProfilePath("https://image.tmdb.org/t/p/original" +
                                                member.getProfilePath());
                        }
                        if (mediaType.equals("movie")) {
                                Movie existingMovie = movieService.findMovieById(Integer.valueOf(movieId));

                                if (existingMovie != null && cast != null) {
                                        existingMovie.setCast(cast);
                                        movieService.saveMovie(existingMovie);
                                }
                        } else if (mediaType.equals("tv")) {
                                TvShow existingTvShow = tvShowService.findTvShowById(Integer.valueOf(movieId));

                                if (existingTvShow != null && cast != null) {
                                        existingTvShow.setCast(cast);
                                        tvShowService.saveTvShow(existingTvShow);
                                }
                        } else {
                                System.out.println("Wrong path.");
                        }

                        // for (CastMember member : cast.getCastMembers()) {
                        // // System.out.println(member.getProfilePath());

                        // if (member.getProfilePath() != null) {
                        // member.setProfilePath("https://api.themoviedb.org/3/movie/"
                        // + member.getProfilePath());
                        // castMemberService.saveMember(member, Integer.valueOf(movieId));

                        // // System.out.println(member.getProfilePath());

                        // }

                        // }
                        List<CastMember> top16Cast = cast.getCast().stream()
                                        .sorted(Comparator.comparingDouble(CastMember::getPopularity).reversed())
                                        .limit(16)
                                        .collect(Collectors.toList());

                        return new ResponseEntity<>(mapper.writeValueAsString(top16Cast), HttpStatus.OK);

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
