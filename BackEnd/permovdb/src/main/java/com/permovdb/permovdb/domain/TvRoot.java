package com.permovdb.permovdb.domain;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TvRoot {
    public int page;
    public ArrayList<TvShow> results;
    public int total_pages;
    public int total_results;
}
