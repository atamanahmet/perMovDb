package com.permovdb.permovdb.domain.DTO;

import java.util.List;
import java.util.Set;

import com.permovdb.permovdb.domain.Movie;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    private Set<Integer> watchlistIdSet;
    private Set<Integer> watchedlistIdSet;
    private Set<Integer> lovedlistIdSet;
    private Set<Movie> watchlist;
    private Set<Movie> watchedlist;
    private Set<Movie> lovedlist;
    private List<Movie> recommendation;
}