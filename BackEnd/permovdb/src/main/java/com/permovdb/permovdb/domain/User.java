package com.permovdb.permovdb.domain;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

import jakarta.validation.constraints.NotBlank;

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

    // public User(String username, String password, String fileName) {
    // this.username = username;
    // this.password = password;
    // this.profileImagePath = fileName;
    // }

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
    @Column(nullable = false, unique = true)
    private String password;

    @ManyToMany
    @JoinTable(name = "user_watchlist", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "movie_id"))
    private Set<Movie> watchlist = new HashSet<>();

    @Column(nullable = true, unique = false)
    private Set<Long> watchListIdSet = new HashSet<>();

    // @Column(nullable = true, unique = true)
    // private String profileImagePath;

}
