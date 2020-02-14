package com.avan.movie.vo;

import com.avan.movie.po.Schedule;
import com.avan.movie.po.Screen;
import com.avan.movie.po.Seat;

import java.util.ArrayList;
import java.util.List;

public class PurchaseQuery {


    private Double totalPrice;

    private List<Integer> seats = new ArrayList<>();

    private Schedule schedule;

    public Double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public List<Integer> getSeats() {
        return seats;
    }

    public void setSeats(List<Integer> seats) {
        this.seats = seats;
    }

    public Schedule getSchedule() {
        return schedule;
    }

    public void setSchedule(Schedule schedule) {
        this.schedule = schedule;
    }

    public PurchaseQuery() {
    }
}
