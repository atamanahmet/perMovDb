package com.permovdb.permovdb.domain;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "movie_cast")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Cast {

    @Id
    @JsonProperty("id")
    private Integer movieId;

    @ElementCollection
    @JsonProperty("cast")
    private ArrayList<CastMember> castMembers;

    @JsonProperty("crew")
    @ElementCollection
    private ArrayList<CastMember> crewMembers;

    @OneToOne
    @JoinColumn(name = "movie_id")
    @MapsId
    private Movie movie;
}