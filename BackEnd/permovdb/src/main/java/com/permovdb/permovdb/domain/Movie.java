package com.permovdb.permovdb.domain;

import java.sql.Date;
import java.util.ArrayList;

import org.springframework.format.annotation.DateTimeFormat;

public class Movie {
    public boolean adult;
    public String backdrop_path;
    public ArrayList<Integer> genre_ids;
    public int id;
    public String original_language;
    public String original_title;
    public String overview;
    public double popularity;
    public String poster_path;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    public Date release_date;

    public String title;
    public boolean video;
    public double vote_average;
    public int vote_count;

}
