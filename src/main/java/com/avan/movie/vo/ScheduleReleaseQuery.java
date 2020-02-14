package com.avan.movie.vo;

import com.avan.movie.po.Movie;
import com.avan.movie.po.Screen;

import java.util.Date;

public class ScheduleReleaseQuery {

//    private Long id;

    private Double price;

    private String beginDate;

    private String beginTime;

    private Movie movie;

    private Screen screen;

    public ScheduleReleaseQuery() {
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(String beginDate) {
        this.beginDate = beginDate;
    }

    public String getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(String beginTime) {
        this.beginTime = beginTime;
    }

    public Movie getMovie() {
        return movie;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }

    public Screen getScreen() {
        return screen;
    }

    public void setScreen(Screen screen) {
        this.screen = screen;
    }
}
