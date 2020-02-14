package com.avan.movie.web.admin;

import com.avan.movie.po.Schedule;
import com.avan.movie.po.Screen;
import com.avan.movie.service.MovieService;
import com.avan.movie.service.OrderService;
import com.avan.movie.service.ScheduleService;
import com.avan.movie.service.ScreenService;
import com.avan.movie.vo.ScheduleQuery;
import com.avan.movie.vo.ScheduleReleaseQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class ScheduleController {

    @Autowired
    private ScheduleService scheduleService;
    @Autowired
    private ScreenService screenService;
    @Autowired
    private MovieService movieService;
    @Autowired
    private OrderService orderService;

    @GetMapping("/schedule_list")
    public String scheduleList(@PageableDefault(size = 8, sort = {"beginTime"}, direction = Sort.Direction.DESC)
                                     Pageable pageable, ScheduleQuery scheduleQuery, Model model) {    //每页5条， 按id倒序(DESC)排列

        List<Schedule> onlineSchedules = scheduleService.getOnlineSchedule();
        for (Schedule onlineSchedule : onlineSchedules) {
            if (onlineSchedule.getEndTime().getTime() <= new Date().getTime()) {
                onlineSchedule.setStat(false);
                scheduleService.saveSchedule(onlineSchedule);
            }
        }

        model.addAttribute("screens", screenService.listScreen());
        model.addAttribute("page", scheduleService.listSchedule(pageable, scheduleQuery));
        return "admin/schedule_list";
    }

    @PostMapping("/schedule_list/search")
    public String scheduleSearch(@PageableDefault(size = 8, sort = {"beginTime"}, direction = Sort.Direction.DESC)
                                       Pageable pageable, ScheduleQuery scheduleQuery, Model model) {
        model.addAttribute("page", scheduleService.listSchedule(pageable, scheduleQuery));
        return "admin/schedule_list :: scheduleList";
    }

    private void setScreensAndMovies(Model model) {
        model.addAttribute("screens", screenService.listScreen());
        model.addAttribute("movies", movieService.listOnlneMovie());
    }

    @GetMapping("/schedule_edit")
    public String scheduleEdit(Model model) {
        setScreensAndMovies(model);
        return "admin/schedule_edit";
    }

    @GetMapping("/{id}/schedule_editPrice")
    public String scheduleUpdate(@PathVariable Long id, Model model) {
        model.addAttribute("schedule", scheduleService.getSchedule(id));
        return "admin/schedule_editPrice";
    }


    @PostMapping("/schedule_save")
    public String scheduleSave(ScheduleReleaseQuery scheduleReleaseQuery, RedirectAttributes attributes) {
        String timestamp = scheduleReleaseQuery.getBeginDate() + " " + scheduleReleaseQuery.getBeginTime();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        try {
            Date timeSchedule = df.parse(timestamp);
            Date timeNow = new Date();
            if (timeSchedule.getTime() < timeNow.getTime()) {
                attributes.addFlashAttribute("message", "请选择正确的放映时间");
                attributes.addFlashAttribute("scheduleReleaseQuery", scheduleReleaseQuery);
                return "redirect:/admin/schedule_edit";
            }
            int durationMs = 60000 * movieService.getMovie(scheduleReleaseQuery.getMovie().getId()).getDuration();
            Date endTime = new Date(timeSchedule.getTime() + durationMs);

            Screen screen = screenService.getScreen(scheduleReleaseQuery.getScreen().getId());
            List<Schedule> onlineSchedules = scheduleService.listOnlineScheduleByScreen(screen);
            for (Schedule s : onlineSchedules ) {
                if (timeSchedule.getTime() >= s.getBeginTime().getTime()
                        && timeSchedule.getTime() <= s.getEndTime().getTime()) {
                    attributes.addFlashAttribute("message", "当前影厅在该时段已被占用，请重新选择放映时间或更换影厅");
                    attributes.addFlashAttribute("scheduleReleaseQuery", scheduleReleaseQuery);
                    return "redirect:/admin/schedule_edit";
                }
            }
            Schedule schedule = new Schedule(
                    scheduleReleaseQuery.getPrice(),
                    timeSchedule, endTime, true,
                    movieService.getMovie(scheduleReleaseQuery.getMovie().getId()),
                    screen);
            Schedule a = scheduleService.saveSchedule(schedule);
            if (a == null) {
                attributes.addFlashAttribute("message", "操作失败");

            } else {
                attributes.addFlashAttribute("message", "操作成功");
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return "redirect:/admin/schedule_list";
    }

    @PostMapping("/schedule_save/{id}")
    public String scheduleUpdate(Schedule schedule,
                               @PathVariable Long id, RedirectAttributes attributes) {

        Schedule temp = scheduleService.getSchedule(schedule.getId());
        temp.setPrice(schedule.getPrice());
        Schedule a = scheduleService.updateSchedule(id, temp);

        if (a == null) {
            attributes.addFlashAttribute("message", "更新失败");
        } else {
            attributes.addFlashAttribute("message", "更新成功");
        }
        return "redirect:/admin/schedule_list";
    }


    @GetMapping("/{id}/schedule_delete")
    public String scheduleDelete(@PathVariable Long id, RedirectAttributes attributes) {
        if (orderService.findBySchedule(scheduleService.getSchedule(id)).size() >= 1) {
            attributes.addFlashAttribute("messageError", "该场次已有部分座位售出，无法取消");
            return "redirect:/admin/schedule_list";
        }
        scheduleService.deleteSchedule(id);
        attributes.addFlashAttribute("message", "取消场次成功");
        return "redirect:/admin/schedule_list";
    }
}
