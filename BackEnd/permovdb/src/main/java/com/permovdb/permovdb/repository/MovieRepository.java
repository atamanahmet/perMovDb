package com.permovdb.permovdb.repository;

import com.permovdb.permovdb.domain.Movie;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MovieRepository extends CrudRepository<Movie, Long> {

    Movie findById(long id);

    boolean existsById(long id);
}
