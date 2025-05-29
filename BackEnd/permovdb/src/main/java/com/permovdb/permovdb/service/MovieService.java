package com.permovdb.permovdb.service;

// import java.io.FileWriter;
// import java.util.ArrayList;
// import java.util.List;

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
            movieRepository.save(movie);
        }
    }

    public Movie findMovieById(Long id) {
        return movieRepository.findById(id).orElse(null);
    }

    // public void saveCSV(Movie movie){
    // try(FileWriter writer = new FileWriter())

    // return

    // }

    // public void saveCurrentRecommendList(Movie movie) {
    // movieBuffer.add(movie);
    // if (movieBuffer.size() >= BATCH_SIZE) {
    // saver.saveBatch(movieBuffer);
    // movieBuffer.clear();
    // }
    // }

    // public void flush() {
    // if (!movieBuffer.isEmpty()) {
    // saver.saveBatch(movieBuffer);
    // movieBuffer.clear();
    // }
    // }
}
