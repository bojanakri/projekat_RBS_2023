package com.zuehlke.securesoftwaredevelopment.domain;

public class Comment {
    private int movieId;
    private Integer userId;
    private String comment;

    public Comment() {
    }

    public Comment(int movieId, Integer userId, String comment) {
        this.movieId = movieId;
        this.userId = userId;
        this.comment = comment;
    }

    public int getMovieId() {
        return movieId;
    }

    public void setMovieId(int carId) {
        this.movieId = carId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
