
package com.permovdb.permovdb.service;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.permovdb.permovdb.domain.TvShow;
import com.permovdb.permovdb.repository.TvShowRepository;

@Service
public class TvShowService {

    @Autowired
    TvShowRepository tvShowRepository;

    public void saveTvShow(TvShow tvShow) {
        tvShowRepository.save(tvShow);
    }

    public TvShow findTvShowById(Integer id) {
        return tvShowRepository.findById(id).orElse(null);
    }

    public List<TvShow> getAllTvShows() {
        return tvShowRepository.findAll();
    }

    public List<TvShow> getTvShowsFromIdSet(Set<Integer> idSet) {
        return tvShowRepository.findAllById(idSet);
    }
}
