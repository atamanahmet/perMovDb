package com.permovdb.permovdb.domain;

import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserDTO {
    private Set<Movie> watchlist;
    private Set<Movie> watchedlist;
    private Set<Movie> lovedlist;
    private Set<Movie> recommendation;
    private Set<Long> watchlistIdSet;
    private Set<Long> watchedlistIdSet;
    private Set<Long> lovedlistIdSet;
}