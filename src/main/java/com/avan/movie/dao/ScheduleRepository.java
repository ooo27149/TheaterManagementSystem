package com.avan.movie.dao;

import com.avan.movie.po.Movie;
import com.avan.movie.po.Schedule;
import com.avan.movie.po.Screen;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ScheduleRepository extends JpaRepository<Schedule, Long>, JpaSpecificationExecutor<Schedule> {


    @Query("select s from Schedule s where s.stat = true order by s.id desc ")
    List<Schedule> onlineSchedule();

    @Query("select s from Schedule s where s.screen = ?1 ")
    List<Schedule> findOnlineScheduleByScreen(Screen screen);

    @Query("select s from Schedule s where s.movie = ?1 ")
    List<Schedule> findScheduleByMovie(Movie movie);
}
