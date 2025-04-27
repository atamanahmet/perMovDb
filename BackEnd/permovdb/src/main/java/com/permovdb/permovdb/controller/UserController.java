package com.permovdb.permovdb.controller;

import org.springframework.web.bind.annotation.RestController;

import com.permovdb.permovdb.annotation.CurrentUser;
import com.permovdb.permovdb.domain.Movie;
import com.permovdb.permovdb.domain.User;
import com.permovdb.permovdb.security.AuthResponse;
import com.permovdb.permovdb.security.JwtCookieUtil;
import com.permovdb.permovdb.service.MovieService;
import com.permovdb.permovdb.service.UserService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
public class UserController {

    @Autowired
    private MovieService movieService;

    @Autowired
    private UserService userService;

    // @Autowired
    // private JwtUtil jwtUtil;

    @Autowired
    private JwtCookieUtil jwtCookieUtil;

    UserController() {
    }

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
        try {
            AuthResponse authResponse = userService.authUser(user);

            if (authResponse == null) {
                return new ResponseEntity<>("Server error. response null.", HttpStatus.EXPECTATION_FAILED);
            }

            jwtCookieUtil.addJwtCookie(response, authResponse.getToken());

            return new ResponseEntity<>(user.getUsername(), HttpStatus.OK);

        } catch (Exception e) {
            System.out.println("error : " + e.getLocalizedMessage());
            return new ResponseEntity<>("Authentication Failed.", HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/user/{username}/watchlist/{id}/{actionType}")
    public ResponseEntity<?> addToWatchList(@PathVariable(name = "id") String id,
            @PathVariable(name = "username") String username, @PathVariable(name = "actionType") String actionType) {

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
    public ResponseEntity<String> getCurrentUser(@CurrentUser String username) {

        System.out.println("/api/me call made. Returned: " + username);

        return new ResponseEntity<>(
                username,
                (username == null) ? HttpStatus.UNAUTHORIZED : HttpStatus.OK);
    }

    @GetMapping("/logout")
    public ResponseEntity<String> logOut(HttpServletResponse response) {

        Cookie cookie = new Cookie("jwt_token", "");

        cookie.setHttpOnly(true);
        cookie.setPath("/");
        response.addCookie(cookie);

        return new ResponseEntity<>("Loged Out", HttpStatus.OK);
    }

}
