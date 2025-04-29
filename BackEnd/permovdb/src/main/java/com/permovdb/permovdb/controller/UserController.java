package com.permovdb.permovdb.controller;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
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

            AuthResponse authResponse = userService.saveUser(user);
            jwtCookieUtil.addJwtCookie(response, authResponse.getToken());

        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.ALREADY_REPORTED);
        }
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody User user, HttpServletResponse response) {
        System.out.println("username" + user.getUsername());
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
            }

            if (!movie.getUserList().contains(user)) {
                movie.getUserList().add(user);
            }
        } else if (actionType.equals("del")) {
            if (user.getWatchlist().contains(movie)) {
                user.getWatchlist().remove(movie);
            }

            if (movie.getUserList().contains(user)) {
                movie.getUserList().remove(user);
            }
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        userService.saveUser(user);
        movieService.saveMovie(movie);

        String response = "";

        for (Movie i : user.getWatchlist()) {
            response = response + " " + i.getId();
        }
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
    public ResponseEntity<String> getWatchlist(HttpServletRequest request) {
        String username = jwtUtil.extractUsernameFromRequest(request);

        User user = userService.loadByUserName(username);

        if (user != null) {
            ObjectMapper mapper = new ObjectMapper();
            try {
                Set<Long> watchListIds = new HashSet<>();
                for (Movie movie : user.getWatchlist()) {
                    watchListIds.add(movie.getId()); // Add only the movie IDs
                }

                return new ResponseEntity<>(mapper.writeValueAsString(watchListIds), HttpStatus.OK);
            } catch (Exception e) {
                System.out.println(e.getLocalizedMessage());
            }
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);

    }

}
