package com.permovdb.permovdb.controller;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.permovdb.permovdb.domain.Movie;
import com.permovdb.permovdb.domain.User;
import com.permovdb.permovdb.security.AuthResponse;
import com.permovdb.permovdb.security.JwtCookieUtil;
import com.permovdb.permovdb.security.JwtUtil;
import com.permovdb.permovdb.service.MovieService;
import com.permovdb.permovdb.service.UserService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

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

    @GetMapping("/signup")
    public String getForm() {
        return new String(
                "To register: POST 127.0.0.1:8080/register, Body={\"username\":\"{yourUsername}\",\"password\":\"{yourPassword}\"}");
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody User user, HttpServletResponse response) {
        try {
            AuthResponse authResponse = new AuthResponse();
            // if (file != null) {
            // authResponse = userService.saveUser(username, password, file);

            // } else {
            // authResponse = userService.saveUser(username, password);
            // }
            System.out.println("authtoken cr3eated: " + authResponse.getToken());
            jwtCookieUtil.addJwtCookie(response, authResponse.getToken());

        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.ALREADY_REPORTED);
        }
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@RequestBody User user, HttpServletResponse response) {
        System.out.println("username" + user.getUsername());
        System.out.println("username" + user.getPassword());
        try {
            AuthResponse authResponse = userService.authUser(user);

            if (authResponse == null) {
                return new ResponseEntity<>("Server error. response null.", HttpStatus.EXPECTATION_FAILED);
            }
            System.out.println("created token: " + authResponse.getToken());

            jwtCookieUtil.addJwtCookie(response, authResponse.getToken());

            return new ResponseEntity<>(authResponse.getUsername(), HttpStatus.OK);

        } catch (Exception e) {
            System.out.println("error : " + e.getLocalizedMessage());
            return new ResponseEntity<>("Authentication Failed.", HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/user/watchlist/{id}/{actionType}")
    public ResponseEntity<?> addToWatchList(@PathVariable(name = "id") String id,
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
            if (!user.getWatchlist().contains(movie)) {
                user.getWatchlist().add(movie);
                user.getWatchListIdSet().add(movie.getId());
            }

            if (!movie.getUserList().contains(user)) {
                movie.getUserList().add(user);
            }
        } else if (actionType.equals("del")) {
            if (user.getWatchlist().contains(movie)) {
                user.getWatchlist().remove(movie);
                user.getWatchListIdSet().remove(movie.getId());
            }

            if (movie.getUserList().contains(user)) {
                movie.getUserList().remove(user);
            }
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        userService.updateUser(user);
        movieService.saveMovie(movie);

        String response = "Removed";

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/api/me")
    public ResponseEntity<String> getCurrentUser(HttpServletRequest request) {
        if (request.getCookies() == null) {
            return new ResponseEntity<>(null, HttpStatus.OK);
        }
        Cookie cookie = jwtCookieUtil.getJWTCookie(request);

        if (cookie != null) {

            String token = cookie.getValue();
            System.out.println("request token = " + token);

            String username = jwtUtil.extractUsername(token);
            System.out.println("Response username to return : " + username);
            if (username == null) {
                return new ResponseEntity<>(username, HttpStatus.OK);

            }
            return new ResponseEntity<>(username, HttpStatus.OK);
        }
        return new ResponseEntity<>("cookie is null", HttpStatus.OK);
    }

    @GetMapping("/user/watchlist")
    public ResponseEntity<Set<Movie>> getWatchlist(HttpServletRequest request) throws JsonProcessingException {
        String username = (jwtUtil.extractUsernameFromRequest(request) != null)
                ? jwtUtil.extractUsernameFromRequest(request)
                : "123";
        // String username = jwtUtil.extractUsernameFromRequest(request);

        User user = userService.loadByUserName(username);
        if (user == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        // ObjectMapper mapper = new ObjectMapper();
        return new ResponseEntity<>(user.getWatchlist(), HttpStatus.OK);
    }

    @GetMapping("/user/watchlistIdSet")
    public ResponseEntity<Set<Long>> getWatchlistIdSet(HttpServletRequest request) throws JsonProcessingException {
        String username = (jwtUtil.extractUsernameFromRequest(request) != null)
                ? jwtUtil.extractUsernameFromRequest(request)
                : "123";

        // String username = jwtUtil.extractUsernameFromRequest(request);

        User user = userService.loadByUserName(username);
        if (user == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        // ObjectMapper mapper = new ObjectMapper();
        return new ResponseEntity<>(user.getWatchListIdSet(), HttpStatus.OK);
    }

}
