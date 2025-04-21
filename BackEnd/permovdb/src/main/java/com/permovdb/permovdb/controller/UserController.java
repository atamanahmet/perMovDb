package com.permovdb.permovdb.controller;

import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.permovdb.permovdb.domain.Movie;
import com.permovdb.permovdb.domain.User;
import com.permovdb.permovdb.security.AuthResponse;
import com.permovdb.permovdb.service.MovieService;
import com.permovdb.permovdb.service.UserService;

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

    UserController() {
    }

    @GetMapping("/signup")
    public String getForm() {
        return new String(
                "To register: POST 127.0.0.1:8080/register, Body={\"username\":\"{yourUsername}\",\"password\":\"{yourPassword}\"}");
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody User user) {
        try {
            userService.saveUser(user);

        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.ALREADY_REPORTED);
        }
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody User user) {
        try {
            System.out.println("username: " + user.getUsername());
            System.out.println("password: " + user.getPassword());
            AuthResponse authResponse = userService.authUser(user);

            if (authResponse.getToken() == null) {
                return new ResponseEntity<>("Token null.", HttpStatus.EXPECTATION_FAILED);
            }
            if (authResponse.getUsername() == null) {
                return new ResponseEntity<>("Username null.", HttpStatus.EXPECTATION_FAILED);

            }

            ObjectMapper mapper = new ObjectMapper();

            String response = mapper.writeValueAsString(authResponse);

            return new ResponseEntity<>(response, HttpStatus.OK);

        } catch (Exception e) {
            System.out.println("error : " + e.getLocalizedMessage());
            return new ResponseEntity<>("Both nonNull but failed", HttpStatus.NOT_FOUND);
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

    @GetMapping("/test")
    public String test() {
        return new String("Successfull");
    }

}
