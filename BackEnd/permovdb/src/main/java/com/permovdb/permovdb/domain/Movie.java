package com.permovdb.permovdb.domain;

import java.sql.Date;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.CascadeType;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "movies")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Movie {

    @Id
    @Column(nullable = false, unique = true)
    private int id;

    @Column
    private boolean adult;

    @Column
    private String backdrop_path;

    @ElementCollection
    @CollectionTable(name = "movie_genres", joinColumns = @JoinColumn(name = "movie_id"))
    @Column(name = "genre_id")
    private List<Integer> genre_ids = new ArrayList<>();

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

    @Column
    private String trailer_path;

    @OneToOne(mappedBy = "movie", cascade = CascadeType.ALL)
    private Cast cast;

}
