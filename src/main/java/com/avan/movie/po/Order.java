package com.avan.movie.po;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "t_order")
public class Order {

    @Id
    @GeneratedValue
    private Long id;

    private Double totalPrice;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createTime;

    @ManyToOne
    private Admin admin;

    @ManyToMany(fetch = FetchType.EAGER)
    private List<Seat> seats = new ArrayList<>();

    @ManyToOne
    private Client client;

    @ManyToOne
    private Schedule schedule;

    public Order() {
    }

    public Order(Double totalPrice, Date createTime, List<Seat> seats, Schedule schedule) {
        this.totalPrice = totalPrice;
        this.createTime = createTime;
        this.seats = seats;
        this.schedule = schedule;
    }

    public Admin getAdmin() {
        return admin;
    }

    public void setAdmin(Admin admin) {
        this.admin = admin;
    }

    public List<Seat> getSeats() {
        return seats;
    }

    public void setSeats(List<Seat> seats) {
        this.seats = seats;
    }

    public Schedule getSchedule() {
        return schedule;
    }

    public void setSchedule(Schedule schedule) {
        this.schedule = schedule;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", totalPrice=" + totalPrice +
                ", createTime=" + createTime +
                ", admin=" + admin +
                ", seats=" + seats +
                ", client=" + client +
                ", schedule=" + schedule +
                '}';
    }
}
