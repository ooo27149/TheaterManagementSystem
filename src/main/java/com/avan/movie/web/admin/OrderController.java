package com.avan.movie.web.admin;

import com.avan.movie.po.*;
import com.avan.movie.service.*;
import com.avan.movie.vo.PurchaseQuery;
import com.avan.movie.vo.OrderQuery;
import com.avan.movie.vo.ScheduleQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Controller
@RequestMapping("/admin")
public class OrderController {


    @Autowired
    private OrderService orderService;
    @Autowired
    private ScheduleService scheduleService;
    @Autowired
    private ScreenService screenService;
    @Autowired
    private SeatService seatService;

    @GetMapping("/order_list")
    public String scheduleList(@PageableDefault(size = 8, sort = {"createTime"}, direction = Sort.Direction.DESC)
                                       Pageable pageable, OrderQuery orderQuery, Model model) {    //每页5条， 按id倒序(DESC)排列

        model.addAttribute("page", orderService.listOrder(pageable, orderQuery));
        return "admin/order_list";
    }

    @PostMapping("/order_list/search")
    public String scheduleSearch(@PageableDefault(size = 8, sort = {"createTime"}, direction = Sort.Direction.DESC)
                                         Pageable pageable, OrderQuery orderQuery, Model model) {
        model.addAttribute("page", orderService.listOrder(pageable, orderQuery));
        return "admin/order_list :: orderList";
    }


    @GetMapping("/order_listSchedule")
    public String scheduleList(@PageableDefault(size = 8, sort = {"beginTime"}, direction = Sort.Direction.DESC)
                                       Pageable pageable, ScheduleQuery scheduleQuery, Model model) {    //每页5条， 按id倒序(DESC)排列


        model.addAttribute("screens", screenService.listScreen());
        model.addAttribute("page", scheduleService.listOnlineSchedule(pageable, scheduleQuery));
        return "admin/order_listSchedule";
    }

    @PostMapping("/order_listSchedule/search")
    public String scheduleSearch(@PageableDefault(size = 8, sort = {"beginTime"}, direction = Sort.Direction.DESC)
                                         Pageable pageable, ScheduleQuery scheduleQuery, Model model) {
        model.addAttribute("page", scheduleService.listOnlineSchedule(pageable, scheduleQuery));
        return "admin/order_listSchedule :: scheduleList";
    }

    @GetMapping("/{id}/order_seat_choose")
    public String seatMapForOrder(@PathVariable Long id, Model model) {

        Schedule schedule = scheduleService.getSchedule(id);
        Screen screen = screenService.getScreen(schedule.getScreen().getId());

        List<Integer> roads = seatService.listRoad(screen);
        List<Integer> maintenances = seatService.listMaintenance(screen);

        model.addAttribute("cols", screen.getColCount());
        model.addAttribute("rows", screen.getRowCount());
        model.addAttribute("schedule", schedule);
        model.addAttribute("roads", roads);

        List<Integer> reserved = new ArrayList<>();

        List<Seat> seats = new ArrayList<>();
        List<Order> orders = orderService.findBySchedule(schedule);
        for (Order o : orders) {
            seats.addAll(o.getSeats());
        }
        for (Seat seat: seats) {
            reserved.add(seatService.findInd(seat));
        }
        reserved.addAll(maintenances);

        model.addAttribute("reserved", reserved);

        return "admin/order_seat_choose";
    }

    @PostMapping("/order_save")
    public String orderSave(PurchaseQuery purchaseQuery, HttpSession session, RedirectAttributes attributes) {

        if (purchaseQuery.getSeats().size() != 0) {
            List<Seat> seats = new ArrayList<>();
            List<Integer> inds = purchaseQuery.getSeats();
            Screen screen = screenService.getScreen(purchaseQuery.getSchedule().getScreen().getId());
            Double totalPrice = 0.0;
            for (Integer ind : inds) {
                seats.add(seatService.getSeatbyScreenAndInd(screen, ind));
                totalPrice += purchaseQuery.getSchedule().getPrice();
            }
            Order order = new Order(totalPrice,
                    new Date(),
                    seats,
                    scheduleService.getSchedule(purchaseQuery.getSchedule().getId()));

            order.setAdmin((Admin) session.getAttribute("admin"));

            Order check = orderService.saveOrder(order);
            if (check != null) {
                attributes.addFlashAttribute("message", "操作成功");
            } else {
                attributes.addFlashAttribute("message1", "操作失败");
            }

            return "redirect:/admin/order_list";
        }

        attributes.addFlashAttribute("message1", "您未选座，提交订单失败");
        return "redirect:/admin/order_list";
    }

    @GetMapping("/{id}/order_delete")
    public String orderDelete(@PathVariable Long id, RedirectAttributes attributes) {
        Order order = orderService.getOrder(id);
        if (order.getAdmin() == null) {
            attributes.addFlashAttribute("message1", "网络售票仅允许顾客自行选择退票");
            return "redirect:/admin/order_list";
        }
        if ((order.getSchedule().getBeginTime().getTime()-7200000) < new Date().getTime()) {
            attributes.addFlashAttribute("message1", "仅允许在开场2小时前退票，取消订单失败");
            return "redirect:/admin/order_list";
        }
        orderService.deleteOrder(id);
        attributes.addFlashAttribute("message", "取消订单成功");
        return "redirect:/admin/order_list";
    }
}
