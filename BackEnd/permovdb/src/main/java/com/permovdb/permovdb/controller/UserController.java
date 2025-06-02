package com.permovdb.permovdb.controller;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.permovdb.permovdb.domain.Movie;
import com.permovdb.permovdb.domain.Root;
import com.permovdb.permovdb.domain.User;
import com.permovdb.permovdb.domain.UserDTO;
import com.permovdb.permovdb.security.AuthResponse;
import com.permovdb.permovdb.security.JwtCookieUtil;
import com.permovdb.permovdb.security.JwtUtil;
import com.permovdb.permovdb.service.MovieService;
import com.permovdb.permovdb.service.UserService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

@RestController
public class UserController {

    @Autowired
    private MovieService movieService;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private JwtCookieUtil jwtCookieUtil;

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/signup")
    public String getForm() {
        return new String(
                "To register: POST 127.0.0.1:8080/register, Body={\"username\":\"{yourUsername}\",\"password\":\"{yourPassword}\"}");
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody User user, HttpServletResponse response) {
        try {
            AuthResponse authResponse = userService.saveUser(user.getUsername(), user.getPassword());

            if (authResponse == null)
                return new ResponseEntity<>("Register error.", HttpStatus.EXPECTATION_FAILED);

            jwtCookieUtil.addJwtCookie(response, authResponse.getToken());

        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.ALREADY_REPORTED);
        }
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@Valid @RequestBody User user, BindingResult bindingResult,
            HttpServletResponse response) {

        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        }
        try {
            AuthResponse authResponse = userService.authUser(user);

            if (authResponse == null) {
                return new ResponseEntity<>("User not found. Please register.", HttpStatus.NOT_FOUND);
            }

            jwtCookieUtil.addJwtCookie(response, authResponse.getToken());

            return new ResponseEntity<>(authResponse.getUsername(), HttpStatus.OK);

        } catch (Exception e) {
            System.out.println("error : " + e.getLocalizedMessage());
            return new ResponseEntity<>("Authentication Failed.", HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/user/list/{type}/{id}/{actionType}")
    public ResponseEntity<?> addToWatchList(
            @PathVariable(name = "id") String id,
            @PathVariable(name = "type") String type,
            @PathVariable(name = "actionType") String actionType,
            HttpServletRequest request) {

        String username = jwtUtil.extractUsernameFromRequest(request);

        Long movieId = (id == null) ? null : Long.valueOf(id);

        if (username == null || movieId == null || actionType == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        User user = userService.loadByUserName(username);

        if (user == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Movie movie = movieService.findMovieById(movieId);

        if (movie == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        if (actionType.equals("add")) {
            if (!user.getWatchlist().contains(movie)) {
                user.getWatchlist().add(movie);
                user.getWatchlistIdSet().add(movie.getId());
            }

            if (!movie.getWatchlistUserSet().contains(user)) {
                movie.getWatchlistUserSet().add(user);
            }
        } else if (actionType.equals("del")) {
            if (user.getWatchlist().contains(movie)) {
                user.getWatchlist().remove(movie);
                user.getWatchlistIdSet().remove(movie.getId());
            }

            if (movie.getWatchlistUserSet().contains(user)) {
                movie.getWatchlistUserSet().remove(user);
            }
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        userService.updateUser(user);
        movieService.saveMovie(movie);

        String response = "Removed";

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    // @GetMapping("/user/watchlist/{id}/{actionType}")
    // public ResponseEntity<?> addToWatchList(@PathVariable(name = "id") String id,
    // @PathVariable(name = "actionType") String actionType, HttpServletRequest
    // request) {

    // String username = jwtUtil.extractUsernameFromRequest(request);

    // Long movieId = (id == null) ? null : Long.valueOf(id);

    // if (username == null || movieId == null || actionType == null) {
    // return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    // }

    // User user = userService.loadByUserName(username);

    // if (user == null) {
    // return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    // }

    // Movie movie = movieService.findMovieById(movieId);

    // if (movie == null) {
    // return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    // }

    // if (actionType.equals("add")) {
    // if (!user.getWatchlist().contains(movie)) {
    // user.getWatchlist().add(movie);
    // user.getWatchlistIdSet().add(movie.getId());
    // }

    // if (!movie.getWatchlistUserSet().contains(user)) {
    // movie.getWatchlistUserSet().add(user);
    // }
    // } else if (actionType.equals("del")) {
    // if (user.getWatchlist().contains(movie)) {
    // user.getWatchlist().remove(movie);
    // user.getWatchlistIdSet().remove(movie.getId());
    // }

    // if (movie.getWatchlistUserSet().contains(user)) {
    // movie.getWatchlistUserSet().remove(user);
    // }
    // } else {
    // return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    // }

    // userService.updateUser(user);
    // movieService.saveMovie(movie);

    // String response = "Removed";

    // return new ResponseEntity<>(response, HttpStatus.OK);
    // }

    @GetMapping("/api/me")
    public ResponseEntity<String> getCurrentUser(HttpServletRequest request) {
        if (request.getCookies() == null) {
            return new ResponseEntity<>(null, HttpStatus.OK);
        }
        Cookie cookie = jwtCookieUtil.getJWTCookie(request);

        if (cookie != null) {

            String token = cookie.getValue();

            String username = jwtUtil.extractUsername(token);
            if (username == null) {
                return new ResponseEntity<>(username, HttpStatus.OK);

            }
            return new ResponseEntity<>(username, HttpStatus.OK);
        }
        return new ResponseEntity<>("cookie is null", HttpStatus.OK);
    }

    @GetMapping("/user/watchlist")
    public ResponseEntity<?> getWatchlist(HttpServletRequest request) throws JsonProcessingException {

        User user = userService.getUserFromRequest(request);

        if (user == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(new ArrayList<>(user.getWatchlist()), HttpStatus.OK);
    }

    @GetMapping("/user/lovedlist")
    public ResponseEntity<?> getLovedlist(HttpServletRequest request) throws JsonProcessingException {

        User user = userService.getUserFromRequest(request);

        if (user == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(new ArrayList<>(user.getLovedlist()), HttpStatus.OK);
    }

    @GetMapping("/user/watchlistIdSet")
    public ResponseEntity<Set<Long>> getWatchlistIdSet(HttpServletRequest request) throws JsonProcessingException {

        User user = userService.getUserFromRequest(request);

        if (user == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(user.getWatchlistIdSet(), HttpStatus.OK);
    }

    @GetMapping("/user/watchedlistIdSet")
    public ResponseEntity<Set<Long>> getWatchedlistIdSet(HttpServletRequest request) throws JsonProcessingException {

        User user = userService.getUserFromRequest(request);

        if (user == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(user.getWatchedlistIdSet(), HttpStatus.OK);
    }

    @GetMapping("/user/lovedlistIdSet")
    public ResponseEntity<Set<Long>> getLovedlistIdSet(HttpServletRequest request) throws JsonProcessingException {

        User user = userService.getUserFromRequest(request);

        if (user == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(user.getLovedlistIdSet(), HttpStatus.OK);
    }

    @GetMapping("/user/watchedlist")
    public ResponseEntity<?> getWatchedlist(HttpServletRequest request) throws JsonProcessingException {

        User user = userService.getUserFromRequest(request);

        if (user == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(new ArrayList<>(user.getWatchedlist()),
                HttpStatus.OK);
    }

    @GetMapping("/user/watchedlist/{id}/{actionType}")
    public ResponseEntity<?> watchedlistEdit(@PathVariable(name = "id") String id,
            @PathVariable(name = "actionType") String actionType, HttpServletRequest request) {

        String username = jwtUtil.extractUsernameFromRequest(request);

        Long movieId = (id == null) ? null : Long.valueOf(id);

        if (username == null || movieId == null || actionType == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        User user = userService.loadByUserName(username);

        if (user == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Movie movie = movieService.findMovieById(movieId);

        if (movie == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        if (actionType.equals("add")) {
            if (!user.getWatchedlist().contains(movie)) {
                user.getWatchedlist().add(movie);
                user.getWatchedlistIdSet().add(movie.getId());
            }

            if (!movie.getWatchedlistUserSet().contains(user)) {
                movie.getWatchedlistUserSet().add(user);
            }
        } else if (actionType.equals("del")) {
            if (user.getWatchedlist().contains(movie)) {
                user.getWatchedlist().remove(movie);
                user.getWatchedlistIdSet().remove(movie.getId());
            }

            if (movie.getWatchedlistUserSet().contains(user)) {
                movie.getWatchedlistUserSet().remove(user);
            }
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        userService.updateUser(user);
        movieService.saveMovie(movie);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/user/lists")
    public ResponseEntity<?> getUserArchive(HttpServletRequest request) {
        User user = userService.getUserFromRequest(request);
        if (user == null)
            return new ResponseEntity<>("User Not found", HttpStatus.NOT_FOUND);

        UserDTO userDTO = new UserDTO(
                user.getWatchlist(),
                user.getWatchedlist(),
                user.getLovedlist(),
                user.getRecommendation(),
                user.getWatchlistIdSet(), // todo remove
                user.getWatchedlistIdSet(), // todo remove
                user.getLovedlistIdSet());// todo remove
        return new ResponseEntity<UserDTO>(userDTO, HttpStatus.OK);
    }

    @GetMapping("/user/lovedlist/{id}/{actionType}")
    public ResponseEntity<?> lovedlistEdit(@PathVariable(name = "id") String id,
            @PathVariable(name = "actionType") String actionType, HttpServletRequest request) {

        String username = jwtUtil.extractUsernameFromRequest(request);

        Long movieId = (id == null) ? null : Long.valueOf(id);

        if (username == null || movieId == null || actionType == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        User user = userService.loadByUserName(username);

        if (user == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Movie movie = movieService.findMovieById(movieId);

        if (movie == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        if (actionType.equals("add")) {
            if (!user.getLovedlist().contains(movie)) {
                user.getLovedlist().add(movie);
                user.getLovedlistIdSet().add(movie.getId());
            }

            if (!movie.getLovedlistUserSet().contains(user)) {
                movie.getLovedlistUserSet().add(user);
            }
        } else if (actionType.equals("del")) {
            if (user.getLovedlist().contains(movie)) {
                user.getLovedlist().remove(movie);
                user.getLovedlistIdSet().remove(movie.getId());
            }

            if (movie.getLovedlistUserSet().contains(user)) {
                movie.getLovedlistUserSet().remove(user);
            }
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        userService.updateUser(user);
        movieService.saveMovie(movie);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/user/upload")
    public ResponseEntity<?> uploadPhoto(@RequestParam("profilePicture") MultipartFile file,
            HttpServletRequest request) throws IOException {

        if (file == null) {
            return new ResponseEntity<>("File is null", HttpStatus.BAD_REQUEST);
        }
        String contentType = file.getContentType();
        if (contentType == null || !List.of("image/jpeg", "image/png").contains(contentType))
            return ResponseEntity.badRequest().body("Unsupported file type");

        String extention = switch (contentType) {
            case "image/jpeg" -> ".jpg";
            case "image/png" -> ".png";
            default -> null;
        };

        if (extention == null) {
            return new ResponseEntity<>(HttpStatus.UNSUPPORTED_MEDIA_TYPE);
        }

        User user = userService.getUserFromRequest(request);

        if (user != null) {

            System.out.println("Photo upload requested");

            String uploadDir = "uploads/profile-pictures/";

            String uuid = UUID.randomUUID().toString();

            String timestamp = String.valueOf(System.currentTimeMillis());

            String fileName = uuid + "_" + timestamp + extention;

            Path filePath = Paths.get(uploadDir + fileName);

            try {
                Files.createDirectories(filePath.getParent());
                Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

                user.setProfilePicturePath(fileName);

                userService.updateUser(user);

                System.out.println("Photo upload successfull");

                return new ResponseEntity<>(HttpStatus.OK);

            } catch (IOException e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("File upload failed");
            }
        }
        return new ResponseEntity<>("User not found. Please login.", HttpStatus.UNAUTHORIZED);
    }

    @GetMapping("/user/photo")
    public ResponseEntity<?> getProfilePhoto(HttpServletRequest request) {
        User user = userService.getUserFromRequest(request);

        if (user == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        String fileName = user.getProfilePicturePath();

        if (fileName == null)
            return ResponseEntity.notFound().build();

        Path path = Paths.get("uploads/profile-pictures/").resolve(fileName);

        if (!Files.exists(path))
            return ResponseEntity.notFound().build();

        String contentType = "";

        try {
            contentType = Files.probeContentType(path);

        } catch (IOException e) {
            System.out.println("Cannot access content type");
            e.printStackTrace();
        }
        try {
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .body(new UrlResource(path.toUri()));
        } catch (MalformedURLException e) {
            System.out.println("Url corrupted?");
            e.printStackTrace();
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);

    }

    @GetMapping("/user/recommendation")
    public ResponseEntity<String> getRecommendation(HttpServletRequest request) {
        return new ResponseEntity<>(sendLovedlistToRec(request), HttpStatus.OK);
    }

    public String sendLovedlistToRec(HttpServletRequest request) {
        User user = userService.getUserFromRequest(request);

        if (user == null) {
            System.out.println("User not exist");
        } else {
            user.setRecommendation(null);

            userService.updateUser(user);

            Set<Movie> lovedSet = user.getLovedlist();

            if (lovedSet != null) {
                List<Map<String, Object>> movieList = new ArrayList<>();
                for (Movie m : lovedSet) {
                    Map<String, Object> movieMap = new HashMap<>();
                    movieMap.put("id", m.getId());
                    movieMap.put("title", m.getTitle());
                    movieMap.put("overview", m.getOverview());
                    movieMap.put("vote_average", m.getVote_average());
                    movieMap.put("genre_ids", m.getGenre_ids());
                    movieMap.put("release_date", m.getRelease_date().toString());
                    movieList.add(movieMap);
                }
                try {
                    ObjectMapper mapper = new ObjectMapper();
                    String json = mapper.writeValueAsString(movieList);

                    HttpHeaders headers = new HttpHeaders();
                    headers.setContentType(MediaType.APPLICATION_JSON);
                    HttpEntity<String> entity = new HttpEntity<>(json, headers);

                    String recEngineUrl = "http://127.0.0.1:8181/rec/update";

                    ResponseEntity<String> res = restTemplate.postForEntity(recEngineUrl, entity, String.class);

                    // List<Movie> recommendationList = mapper.readValue(res.getBody(), new
                    // TypeReference<List<Movie>>() {
                    // });
                    // System.out.println(res.getBody());

                    List<Movie> recList = mapper.readValue(res.getBody(), new TypeReference<List<Movie>>() {
                    });

                    recList.sort(Comparator.comparing(Movie::getVote_average).reversed());

                    user.setRecommendation(recList);
                    userService.updateUser(user);

                    // for (Movie movie : root) {
                    // System.out.println(movie.getTitle());
                    // }

                    return res.getBody();
                } catch (Exception e) {
                    System.out.println(e.getLocalizedMessage());
                    e.printStackTrace();
                }
            }
        }
        return "Content cannot be found ";

    }

}
