package com.permovdb.permovdb.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
@Table(name = "CastMember")
public class CastMember {

    @Id
    @Column
    private Integer id;
    @Column
    private String name;
    @Column
    private String originalName;
    @Column
    private boolean adult;
    @Column
    private int gender;
    @Column
    private String knownForDepartment; // Acting, Directing etc.

    @ElementCollection
    @CollectionTable(name = "member_castMap", joinColumns = @JoinColumn(name = "movie_id"))
    private Map<Integer, String> castMap;

    @Transient
    private String job;

    @Transient
    private String character;

    @Column
    private String creditId;
    @Column(name = "cast_order")
    private int order;
    @Column
    private double popularity;
    @Column
    private String profilePath;
}
