package com.avan.movie.service;

import com.avan.movie.po.Movie;
import com.avan.movie.po.Schedule;
import com.avan.movie.po.Screen;
import com.avan.movie.vo.ScheduleQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ScheduleService {

    Schedule saveSchedule(Schedule schedule);

    Schedule getSchedule(Long id);

    Page<Schedule> listSchedule(Pageable pageable, ScheduleQuery scheduleQuery);

    Schedule updateSchedule(Long id, Schedule schedule);

    void deleteSchedule(Long id);

    List<Schedule> getOnlineSchedule();

    Page<Schedule> listOnlineSchedule(Pageable pageable, ScheduleQuery scheduleQuery);

    List<Schedule> listOnlineScheduleByScreen(Screen screen);

    List<Schedule> listScheduleByMovie(Movie movie);
}
