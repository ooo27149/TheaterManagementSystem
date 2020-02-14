package com.avan.movie.web.admin;

import com.avan.movie.po.Screen;
import com.avan.movie.po.Seat;
import com.avan.movie.service.ScreenService;
import com.avan.movie.service.SeatService;
import com.avan.movie.vo.SeatQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class SeatController {

    @Autowired
    private SeatService seatService;

    @Autowired
    private ScreenService screenService;

    @GetMapping("/seat_edit/{id}")
    public String seatMap(@PathVariable Long id,Model model) {

        Screen screen = screenService.getScreen(id);
        Integer rows = screen.getRowCount();
        Integer cols = screen.getColCount();

        List<Integer> maintenances = seatService.listMaintenance(screen);
        List<Integer> roads = seatService.listRoad(screen);

        model.addAttribute("rows", rows);
        model.addAttribute("cols", cols);
        model.addAttribute("maintenances", maintenances);
        model.addAttribute("roads", roads);
        model.addAttribute("screen", screen);

        return "admin/seat_edit";
    }

    @PostMapping("/seat_edit/save")
    public String seatMapSave(SeatQuery seatQuery, RedirectAttributes attributes) {
        Screen screen = screenService.getScreen(seatQuery.getScreenId());
        screen.setSeatCount(screen.getColCount() * screen.getRowCount());

        List<Integer> oldMaintenance = seatService.listMaintenance(screen);
        List<Integer> oldRoad = seatService.listRoad(screen);
        for (Integer integer : oldMaintenance) {
            Seat seat = seatService.getSeatbyScreenAndInd(screen, integer);
            seat.setMaintenance(false);
            seatService.updateSeat(seat.getId(), seat);
        }
        for (Integer integer : oldRoad) {
            Seat seat = seatService.getSeatbyScreenAndInd(screen, integer);
            seat.setRoad(false);
            seatService.updateSeat(seat.getId(), seat);
        }

        if (seatQuery.getMaintenances().size() != 0) {
            List<Integer> newMaintenance = seatQuery.getMaintenances();
            for (Integer integer : newMaintenance) {
                Seat seat = seatService.getSeatbyScreenAndInd(screen, integer);
                System.out.println(seat);
                seat.setMaintenance(true);
                seatService.updateSeat(seat.getId(), seat);
                screen.setSeatCount(screen.getSeatCount()-1);
            }
            attributes.addFlashAttribute("message", "操作成功");

        }
        if (seatQuery.getRoads().size() != 0) {
            List<Integer> newRoad = seatQuery.getRoads();
            for (Integer integer : newRoad) {
                Seat seat = seatService.getSeatbyScreenAndInd(screen, integer);
                seat.setRoad(true);
                seatService.updateSeat(seat.getId(), seat);
                screen.setSeatCount(screen.getSeatCount()-1);
            }
            attributes.addFlashAttribute("message", "操作成功");
        }
        return "redirect:/admin/screen_list";
    }
}
