package com.permovdb.permovdb.domain;

import java.util.ArrayList;
import java.util.List;

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
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "users")
@Getter
@Setter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Username can not be empty")
    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false, unique = true)
    private String JWToken;

    @NotBlank(message = "Password can not be empty")
    @Column(nullable = false, unique = true)
    private String password;

    @ManyToMany
    @JoinTable(name = "user_watchlist", // The name of the join table
            joinColumns = @JoinColumn(name = "user_id"), // The column for User's ID
            inverseJoinColumns = @JoinColumn(name = "movie_id") // The column for Movie's ID
    )
    private List<Movie> watchlist = new ArrayList<>();
}
