package com.avan.movie.web.admin;

import com.avan.movie.po.Screen;
import com.avan.movie.po.Seat;
import com.avan.movie.service.ScheduleService;
import com.avan.movie.service.ScreenService;
import com.avan.movie.service.SeatService;
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

@Controller
@RequestMapping("/admin")
public class ScreenController {

    @Autowired
    private ScreenService screenService;
    @Autowired
    private ScheduleService scheduleService;
    @Autowired
    private SeatService seatService;

    @GetMapping("/screen_list")
    public String screenList(@PageableDefault(size = 8, sort = {"id"}, direction = Sort.Direction.DESC)
                                    Pageable pageable, Screen screen, Model model) {    //每页5条， 按id倒序(DESC)排列
        model.addAttribute("page", screenService.listScreen(pageable, screen));
        return "admin/screen_list";
    }
    @PostMapping("/screen_list/search")
    public String screenSearch(@PageableDefault(size = 8, sort = {"id"}, direction = Sort.Direction.DESC)
                                      Pageable pageable, Screen screen, Model model) {
        model.addAttribute("page", screenService.listScreen(pageable, screen));
        return "admin/screen_list :: screenList";
    }

    @GetMapping("/screen_edit")
    public String screenEdit(Model model) {
        model.addAttribute("screen", new Screen());
        return "admin/screen_edit";
    }

    @GetMapping("/{id}/screen_edit")
    public String screenUpdate(@PathVariable Long id, Model model) {
        model.addAttribute("screen", screenService.getScreen(id));
        return "admin/screen_edit";
    }


    @PostMapping("/screen_save")
    public String screenSave(@Valid Screen screen, BindingResult bindingResult,
                             RedirectAttributes attributes) {
        Screen temp = screenService.getScreenByName(screen.getName());
        if (temp != null) {
            bindingResult.rejectValue("screen", "screenError", "该影厅已存在");
        }
        if (bindingResult.hasErrors()) {
            return "admin/screen_edit";
        }
        screen.setSeatCount(screen.getColCount()*screen.getRowCount());
        Screen a = screenService.saveScreen(screen);
        int i, j, index = 0;
        for(i = 0; i <= screen.getRowCount(); i++){
            for (j = 1; j <= screen.getColCount(); j++){
                Seat seat = new Seat(i, j, false, false, index, screen);
                index++;
                seatService.saveSeat(seat);
            }
        }
        if (a == null) {
            attributes.addFlashAttribute("message", "操作失败");
        } else {
            attributes.addFlashAttribute("message", "操作成功");
        }

        return "redirect:/admin/screen_list";
    }


    @PostMapping("/screen_save/{id}")
    public String screenUpdate(@Valid Screen screen,
                              @PathVariable Long id, RedirectAttributes attributes) {
        screen.setSeatCount(screen.getColCount()*screen.getRowCount());
        Screen a = screenService.updateScreen(id, screen);
        if (a == null) {
            attributes.addFlashAttribute("message", "更新失败");
        } else {
            attributes.addFlashAttribute("message", "更新成功");
        }
        return "redirect:/admin/screen_list";
    }


    @GetMapping("/{id}/screen_delete")
    public String screenDelete(@PathVariable Long id, RedirectAttributes attributes) {
        if (scheduleService.listOnlineScheduleByScreen(screenService.getScreen(id)).size() >= 1) {
            attributes.addFlashAttribute("messageError", "该影厅已被部分场次占用，无法删除");
            return "redirect:/admin/screen_list";
        }
        screenService.deleteScreen(id);
        attributes.addFlashAttribute("message", "删除成功");
        return "redirect:/admin/screen_list";
    }
}
