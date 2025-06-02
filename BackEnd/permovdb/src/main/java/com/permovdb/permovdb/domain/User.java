package com.permovdb.permovdb.domain;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "users")
@Getter
@Setter
@AllArgsConstructor
public class User {
    public User() {

    }

    public User(User user) {
        this.username = user.getUsername();
        this.password = user.getPassword();
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Username can not be empty")
    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = true, unique = true)
    private String JWToken;

    @NotBlank(message = "Password can not be empty")
    @Size(min = 3)
    @Column(nullable = false, unique = true)
    private String password;

    @Column(nullable = true, unique = true)
    private String profilePicturePath;

    @ManyToMany
    @JoinTable(name = "user_watchlist", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "movie_id"))
    private Set<Movie> watchlist = new HashSet<>();

    @Column(nullable = true, unique = false)
    private Set<Long> watchlistIdSet = new HashSet<>();

    @ManyToMany
    @JoinTable(name = "user_watchedlist", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "movie_id"))
    @Column(nullable = true, unique = false)
    private Set<Movie> watchedlist = new HashSet<>();

    @ManyToMany
    @JoinTable(name = "user_lovedlist", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "movie_id"))
    @Column(nullable = true, unique = false)
    private Set<Movie> lovedlist = new HashSet<>();

    @Column(nullable = true, unique = false)
    private Set<Long> watchedlistIdSet = new HashSet<>();

    @Column(nullable = true, unique = false)
    private Set<Long> lovedlistIdSet = new HashSet<>();

    @ElementCollection
    @CollectionTable(name = "user_search_entries", joinColumns = @JoinColumn(name = "user_id"))
    private List<SearchEntry> searchDataWithDate = new ArrayList<>();

    @ManyToMany
    @JoinTable(name = "user_recommendation", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "movie_id"))
    private List<Movie> recommendation = new ArrayList<>();

}
