package com.permovdb.permovdb.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.permovdb.permovdb.domain.Movie;
import com.permovdb.permovdb.repository.MovieRepository;

@Service
public class MovieService {

    @Autowired
    MovieRepository movieRepository;

    public void saveMovie(Movie movie) {

        if (!movieRepository.existsById(movie.getId())) {
            // System.out.println("movie already exist.-" + movie.getId() + "-" +
            // movie.getTitle());
            movieRepository.save(movie);
        }
    }

    public Movie findMovieById(Long id) {
        return movieRepository.findById(id).orElse(null);
    }
}
