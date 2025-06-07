package com.permovdb.permovdb.service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.permovdb.permovdb.domain.Movie;
import com.permovdb.permovdb.repository.MovieRepository;

@Service
public class MovieService {

    @Autowired
    MovieRepository movieRepository;

    public void saveOrUpdateMovie(Movie movie) {
        Optional<Movie> existingMovieOpt = movieRepository.findById(movie.getId());
        if (existingMovieOpt.isPresent()) {
            Movie existingMovie = existingMovieOpt.get();

            existingMovie.setAdult(movie.isAdult());
            existingMovie.setBackdrop_path(movie.getBackdrop_path());
            existingMovie.setGenre_ids(movie.getGenre_ids());
            existingMovie.setOriginal_language(movie.getOriginal_language());
            existingMovie.setOriginal_title(movie.getOriginal_title());
            existingMovie.setOverview(movie.getOverview());
            existingMovie.setPopularity(movie.getPopularity());
            existingMovie.setPoster_path(movie.getPoster_path());
            existingMovie.setRelease_date(movie.getRelease_date());
            existingMovie.setTitle(movie.getTitle());
            existingMovie.setVideo(movie.isVideo());
            existingMovie.setVote_average(movie.getVote_average());
            existingMovie.setVote_count(movie.getVote_count());
            existingMovie.setTrailer_path(movie.getTrailer_path());

            movieRepository.save(existingMovie);
        } else {
            movieRepository.save(movie);
        }
    }

    public void saveMovie(Movie movie) {
        saveOrUpdateMovie(movie);
    }

    public Movie findMovieById(Integer id) {
        return movieRepository.findById(id).orElse(null);
    }

    public List<Movie> getAllMovies() {
        return movieRepository.findAll();
    }

    public List<Movie> getMoviesFromIdSet(Set<Integer> idSet) {
        return movieRepository.findAllById(idSet);
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
