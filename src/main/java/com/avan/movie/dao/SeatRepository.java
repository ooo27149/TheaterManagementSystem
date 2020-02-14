package com.avan.movie.dao;

import com.avan.movie.po.Screen;
import com.avan.movie.po.Seat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SeatRepository extends JpaRepository<Seat, Long>, JpaSpecificationExecutor<Seat> {

    @Query("select s.ind from Seat s where s.screen = ?1 and s.maintenance = true ")
    List<Integer> findByScreenIdAndMaintenance(Screen screen);

    @Query("select s.ind from Seat s where s.screen = ?1 and s.road = true ")
    List<Integer> findByScreenAndRoad(Screen screen);

    @Query("select s from Seat s where s.screen = ?1 and s.ind = ?2 ")
    Seat findByScreenAndInd(Screen screen, int ind);

    @Query("select s.ind from Seat s where s = ?1 ")
    Integer findInd(Seat seat);
}
