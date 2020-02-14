package com.avan.movie.po;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "t_schedule")
public class Schedule {

    @Id
    @GeneratedValue
    private Long id;

    private Double price;

    @Temporal(TemporalType.TIMESTAMP)
    private Date beginTime;

    @Temporal(TemporalType.TIMESTAMP)
    private Date endTime;

    private Boolean stat;

    @ManyToOne
    private Movie movie;

    @ManyToOne
    private Screen screen;

    @OneToMany(mappedBy = "schedule")
    private List<Order> orders = new ArrayList<>();

    public Schedule() {
    }

    public Schedule(Double price, Date beginTime, Date endTime, Boolean stat, Movie movie, Screen screen) {
        this.price = price;
        this.beginTime = beginTime;
        this.endTime = endTime;
        this.stat = stat;
        this.movie = movie;
        this.screen = screen;
    }

    public Boolean getStat() {
        return stat;
    }

    public void setStat(Boolean stat) {
        this.stat = stat;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
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

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Date getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(Date beginTime) {
        this.beginTime = beginTime;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }
}
