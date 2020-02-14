package com.avan.movie.vo;

public class ScheduleQuery {

    private String movieName;
    private Long screenId;

    public String getMovieName() {
        return movieName;
    }

    public void setMovieName(String movieName) {
        this.movieName = movieName;
    }

    public Long getScreenId() {
        return screenId;
    }

    public void setScreenId(Long screenId) {
        this.screenId = screenId;
    }

    public ScheduleQuery() {
    }
}
