package com.avan.movie.web;

import com.avan.movie.po.Schedule;
import com.avan.movie.service.MovieService;
import com.avan.movie.service.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Date;
import java.util.List;

@Controller
public class ShowingController {

    @Autowired
    private MovieService movieService;
    @Autowired
    private ScheduleService scheduleService;

    @GetMapping("/showing")
    public String listShowing(Model model) {
        List<Schedule> onlineSchedules = scheduleService.getOnlineSchedule();
        for (Schedule onlineSchedule : onlineSchedules) {
            if (onlineSchedule.getEndTime().getTime() <= new Date().getTime()) {
                onlineSchedule.setStat(false);
                scheduleService.saveSchedule(onlineSchedule);
            }
        }
        model.addAttribute("movies", movieService.listOnshowingMovie());
        model.addAttribute("schedules", scheduleService.getOnlineSchedule());
        return "showing";
    }



}
