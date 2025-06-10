package com.permovdb.permovdb.repository;

import com.permovdb.permovdb.domain.TvShow;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TvShowRepository extends JpaRepository<TvShow, Integer> {

}
