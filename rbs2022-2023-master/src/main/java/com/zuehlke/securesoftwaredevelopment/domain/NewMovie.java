package com.zuehlke.securesoftwaredevelopment.domain;

import java.util.List;

public class NewMovie {

    private String title;

    private String description;

    private List<Integer> genres;

    public NewMovie(String title, String description, List<Integer> genres) {
        this.title = title;
        this.description = description;
        this.genres = genres;
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

    public List<Integer> getGenres() {
        return genres;
    }

    public void setGenres(List<Integer> genres) {
        this.genres = genres;
    }
}
