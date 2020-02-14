package com.avan.movie.service;

import com.avan.movie.po.Screen;
import com.avan.movie.po.Seat;

import java.util.List;

public interface SeatService {

    Seat saveSeat(Seat seat);

    Seat getSeat(Long id);

    List<Seat> listSeats();

    Seat updateSeat(Long id, Seat seat);

    List<Integer> listMaintenance(Screen screen);

    List<Integer> listRoad(Screen screen);

    Seat getSeatbyScreenAndInd(Screen screen, int ind);

    Integer findInd(Seat seat);
}
