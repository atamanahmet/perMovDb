package com.permovdb.permovdb.domain;

import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    private Set<Long> watchlistIdSet;
    private Set<Long> watchedlistIdSet;
    private Set<Long> lovedlistIdSet;
    private Set<Movie> watchlist;
    private Set<Movie> watchedlist;
    private Set<Movie> lovedlist;
    private Set<Movie> recommendation;
}