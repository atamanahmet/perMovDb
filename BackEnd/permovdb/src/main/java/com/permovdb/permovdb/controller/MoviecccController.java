// package com.permovdb.permovdb.controller;

// import java.util.ArrayList;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.http.ResponseEntity;
// import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.PathVariable;
// import org.springframework.web.bind.annotation.PostMapping;
// import org.springframework.web.bind.annotation.RequestBody;
// import org.springframework.web.bind.annotation.RestController;

// import com.permovdb.permovdb.domain.Movie;
// import com.permovdb.permovdb.service.MovieService;

// @RestController
// public class MovieController {

// @Autowired
// private MovieService movieService;

// @PostMapping("/movie")
// public ResponseEntity<String> getMovieFromId(@RequestBody ArrayList<Long>
// idList){

// Movie movie = movieService.findMovieById(id);

// }
// }
