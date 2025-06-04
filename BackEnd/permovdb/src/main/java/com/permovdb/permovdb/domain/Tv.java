// package com.permovdb.permovdb.domain;

// import java.util.List;

// import org.springframework.format.annotation.DateTimeFormat;

// import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

// import jakarta.persistence.Column;
// import jakarta.persistence.Entity;
// import jakarta.persistence.Id;
// import jakarta.persistence.Table;
// import lombok.AllArgsConstructor;
// import lombok.Getter;
// import lombok.Setter;

// @Getter
// @Setter
// @AllArgsConstructor
// @Entity
// @Table(name = "movies")
// @JsonIgnoreProperties(ignoreUnknown = true)
// public class Tv {

// @Column
// private boolean adult;

// @Column
// private String backdropPath;

// @Column
// private List<Integer> genreIds;

// @Id
// @Column(nullable = false, unique = true)
// private long id;

// @Column
// private List<String> originCountry;

// @Column
// private String originalLanguage;

// @Column
// private String originalName;

// @Column(length = 2048)
// private String overview;

// @Column
// private double popularity;

// @Column
// private String posterPath;

// @Column
// @DateTimeFormat(pattern = "yyyy-MM-dd")
// private String firstAirDate;

// @Column
// private String name;

// @Column
// private double voteAverage;

// @Column
// private int voteCount;

// }
