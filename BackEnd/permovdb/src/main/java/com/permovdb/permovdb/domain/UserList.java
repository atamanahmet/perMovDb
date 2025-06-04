package com.permovdb.permovdb.domain;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embeddable;
import jakarta.persistence.JoinColumn;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserList {
    @ElementCollection
    @CollectionTable(name = "user_movie_watchlist", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "movie_id")
    private Set<Long> movieWatchlist = new HashSet<>();

    @ElementCollection
    @CollectionTable(name = "user_movie_watchedlist", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "movie_id")
    private Set<Long> movieWatchedlist = new HashSet<>();

    @ElementCollection
    @CollectionTable(name = "user_movie_lovedlist", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "movie_id")
    private Set<Long> movieLovedlist = new HashSet<>();

    @ElementCollection
    @CollectionTable(name = "user_tv_watchlist", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "tv_id")
    private Set<Long> tvWatchlist = new HashSet<>();

    @ElementCollection
    @CollectionTable(name = "user_tv_watchedlist", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "tv_id")
    private Set<Long> tvWatchedlist = new HashSet<>();

    @ElementCollection
    @CollectionTable(name = "user_tv_lovedlist", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "tv_id")
    private Set<Long> tvLovedlist = new HashSet<>();
}
