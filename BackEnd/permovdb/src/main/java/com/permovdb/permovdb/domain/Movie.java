package com.permovdb.permovdb.domain;

import java.sql.Date;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "movies")
@Getter
@Setter
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Movie {

    public Movie() {
    }

    @Id
    @Column(nullable = false, unique = true)
    private long id;

    @Column
    private boolean adult;

    @Column
    private String backdrop_path;

    @Column
    private ArrayList<Integer> genre_ids;

    @Column
    private String original_language;

    @Column
    private String original_title;

    @Column(length = 2048)
    private String overview;

    @Column
    private double popularity;

    @Column
    private String poster_path;

    @Column
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date release_date;

    @Column
    private String title;

    @Column
    private boolean video;

    @Column
    private double vote_average;

    @Column
    private int vote_count;

    @JsonIgnore
    @ManyToMany(mappedBy = "watchlist")
    private Set<User> watchlistUserSet = new HashSet<>();

    @JsonIgnore
    @ManyToMany(mappedBy = "watchedlist")
    private Set<User> watchedlistUserSet = new HashSet<>();

    @JsonIgnore
    @ManyToMany(mappedBy = "lovedlist")
    private Set<User> lovedlistUserSet = new HashSet<>();

}
