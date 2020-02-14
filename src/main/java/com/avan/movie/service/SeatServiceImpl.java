package com.avan.movie.service;

import com.avan.movie.NotFoundException;
import com.avan.movie.dao.SeatRepository;
import com.avan.movie.po.Screen;
import com.avan.movie.po.Seat;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SeatServiceImpl implements SeatService {

    @Autowired
    private SeatRepository seatRepository;

    @Transactional
    @Override
    public Seat saveSeat(Seat seat) {
        return seatRepository.save(seat);
    }

    @Transactional
    @Override
    public Seat getSeat(Long id) {
        return seatRepository.findOne(id);
    }

    @Transactional
    @Override
    public List<Seat> listSeats() {
        return seatRepository.findAll();
    }

    @Transactional
    @Override
    public Seat updateSeat(Long id, Seat seat) {
        Seat temp = seatRepository.findOne(id);
        if (temp == null) {
            throw new NotFoundException("the seat doesn't exist");
        }
        BeanUtils.copyProperties(seat, temp);
        return seatRepository.save(temp);
    }

    @Override
    public List<Integer> listMaintenance(Screen screen) {
        return seatRepository.findByScreenIdAndMaintenance(screen);
    }

    @Override
    public List<Integer> listRoad(Screen screen) {
        return seatRepository.findByScreenAndRoad(screen);
    }

    @Override
    public Seat getSeatbyScreenAndInd(Screen screen, int ind) {
        return seatRepository.findByScreenAndInd(screen, ind);
    }

    @Override
    public Integer findInd(Seat seat) {
        return seatRepository.findInd(seat);
    }
}
