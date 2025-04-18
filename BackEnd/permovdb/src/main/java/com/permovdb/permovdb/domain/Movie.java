package com.permovdb.permovdb.domain;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "movies")
@Getter
@Setter
public class Movie {

    // @Id
    // @GeneratedValue(strategy = GenerationType.SEQUENCE)
    // @Column(nullable = false, unique = true)
    // private long dbId;

    @Column
    private boolean adult;

    @Column
    private String backdrop_path;

    @Column
    private ArrayList<Integer> genre_ids;

    @Id
    @Column
    private long id;

    @Column
    private String original_language;

    @Column
    private String original_title;

    @Column(length = 512)
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

    @ManyToMany(mappedBy = "watchlist")
    private List<User> userList = new ArrayList<>();

}
