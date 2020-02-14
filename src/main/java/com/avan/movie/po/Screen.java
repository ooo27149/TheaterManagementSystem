package com.avan.movie.po;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "t_screen")
public class Screen {

    @Id
    @GeneratedValue
    private Long id;

    private String name;
    private String info;

    private Integer rowCount;
    private Integer colCount;
    private Integer seatCount;

    @OneToMany(mappedBy = "screen")
    private List<Schedule> schedules = new ArrayList<>();


    @OneToMany(mappedBy = "screen", cascade = CascadeType.ALL)
    private List<Seat> seats = new ArrayList<>();

    public Screen() {
    }

    public Integer getSeatCount() {
        return seatCount;
    }

    public void setSeatCount(Integer seatsCount) {
        this.seatCount = seatsCount;
    }

    public List<Schedule> getSchedules() {
        return schedules;
    }

    public void setSchedules(List<Schedule> schedules) {
        this.schedules = schedules;
    }

    public List<Seat> getSeats() {
        return seats;
    }

    public void setSeats(List<Seat> seats) {
        this.seats = seats;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public Integer getRowCount() {
        return rowCount;
    }

    public void setRowCount(Integer rowCount) {
        this.rowCount = rowCount;
    }

    public Integer getColCount() {
        return colCount;
    }

    public void setColCount(Integer colCount) {
        this.colCount = colCount;
    }


}
