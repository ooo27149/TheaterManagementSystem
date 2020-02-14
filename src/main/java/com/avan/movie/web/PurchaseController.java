package com.avan.movie.web;

import com.avan.movie.po.*;
import com.avan.movie.service.OrderService;
import com.avan.movie.service.ScheduleService;
import com.avan.movie.service.ScreenService;
import com.avan.movie.service.SeatService;
import com.avan.movie.vo.PurchaseQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
public class PurchaseController {

    @Autowired
    private OrderService orderService;
    @Autowired
    private SeatService seatService;
    @Autowired
    private ScreenService screenService;
    @Autowired
    private ScheduleService scheduleService;

    @GetMapping("/client/purchase/{id}")
    public String seatMap(@PathVariable Long id, Model model) {

        Schedule schedule = scheduleService.getSchedule(id);
        Screen screen = screenService.getScreen(schedule.getScreen().getId());

        List<Integer> maintenances = seatService.listMaintenance(screen);
        List<Integer> roads = seatService.listRoad(screen);

        model.addAttribute("rows", screen.getRowCount());
        model.addAttribute("cols", screen.getColCount());
        model.addAttribute("roads", roads);
        model.addAttribute("schedule", schedule);

        List<Integer> reserved = new ArrayList<>();
        reserved.addAll(maintenances);

        List<Seat> seats = new ArrayList<>();
        List<Order> orders = orderService.findBySchedule(schedule);
        for (Order o : orders) {
            seats.addAll(o.getSeats());
        }
        for (Seat seat: seats) {
            reserved.add(seatService.findInd(seat));
        }

        model.addAttribute("reserved", reserved);
        return "seat_map";
    }


    @PostMapping("/client/order_confirm")
    public String orderConfirm(PurchaseQuery purchaseQuery, HttpSession session, Model model, RedirectAttributes attributes) {

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

            order.setClient((Client) session.getAttribute("client"));
            purchaseQuery.setTotalPrice(totalPrice);

            session.setAttribute("order",order);
            model.addAttribute("purchaseQuery", purchaseQuery);
            model.addAttribute("order", order);


            return "order_confirm";
        }

        attributes.addFlashAttribute("message1", "您未选座，提交订单失败");
        return "redirect:/showing";
    }

    @GetMapping("/client/order_save")
    public String orderSave(HttpSession session, RedirectAttributes attributes) {

        Order check = orderService.saveOrder((Order) session.getAttribute("order"));
        if (check != null) {
            attributes.addFlashAttribute("message", "操作成功");
            session.removeAttribute("order");
            return "redirect:/client/orders";
        } else {

            attributes.addFlashAttribute("message1", "提交订单失败,请稍后重试");

        }

        return "redirect:/showing";
    }

    @GetMapping("/client/orders")
    public String orders(HttpSession session, Model model) {

        Client client = (Client) session.getAttribute("client");
        List<Order> orders = orderService.listOrderByClient(client);
        if (orders.size() < 1) {
            model.addAttribute("message1","暂无购票记录");
        }
        model.addAttribute("orders", orders);

        return "orders";
    }

    @GetMapping("/client/order_delete/{id}")
    public String orderDelete(@PathVariable Long id, RedirectAttributes attributes) {
        if ((orderService.getOrder(id).getSchedule().getBeginTime().getTime()-7200000) < new Date().getTime()) {
            attributes.addFlashAttribute("messageError", "仅允许在开场2小时前退票，取消订单失败");
            return "redirect:/client/orders";
        }
        orderService.deleteOrder(id);
        attributes.addFlashAttribute("message", "取消订单成功");
        return "redirect:/client/orders";
    }
}
