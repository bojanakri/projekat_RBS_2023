package com.zuehlke.securesoftwaredevelopment.domain;

import java.util.List;

public class Movie {

    private int id;

    private String title;

    private String description;

    private List<Genre> genres;

    public Movie(int id, String title, String description, List<Genre> genres) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.genres = genres;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Genre> getGenres() {
        return genres;
    }

    public void setGenres(List<Genre> genres) {
        this.genres = genres;
    }
}
