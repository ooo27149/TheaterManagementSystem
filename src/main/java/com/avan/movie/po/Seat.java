package com.avan.movie.po;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "t_seat")
public class Seat {

    @Id
    @GeneratedValue
    private Long id;

    private int row;
    private int col;
    private Boolean maintenance;
    private Boolean road;
    private int ind;

    @ManyToMany(mappedBy = "seats")
    private List<Order> orders = new ArrayList<>();

    @ManyToOne
    private Screen screen;

    public Seat() {
    }

    public Seat(int row, int col, Boolean maintenance, Boolean road, int index, Screen screen) {
        this.row = row;
        this.col = col;
        this.maintenance = maintenance;
        this.road = road;
        this.ind = index;
        this.screen = screen;
    }

    public Boolean getMaintenance() {
        return maintenance;
    }

    public void setMaintenance(Boolean maintenance) {
        this.maintenance = maintenance;
    }

    public Boolean getRoad() {
        return road;
    }

    public void setRoad(Boolean road) {
        this.road = road;
    }

    public int getInd() {
        return ind;
    }

    public void setInd(int ind) {
        this.ind = ind;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public Screen getScreen() {
        return screen;
    }

    public void setScreen(Screen screen) {
        this.screen = screen;
    }

    @Override
    public String toString() {

        return  " " + ((char)(row % 26 + (int) 'A')) + "排" + col + "座 " + " ";
    }
}
