package com.avan.movie.service;

import com.avan.movie.po.Client;
import com.avan.movie.po.Order;
import com.avan.movie.po.Schedule;
import com.avan.movie.vo.OrderQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface OrderService {

    Order saveOrder(Order order);

    Order getOrder(Long id);

    Order updateOrder(Long id, Order order);

    void deleteOrder(Long id);

    Page<Order> listOrder(Pageable pageable, OrderQuery orderQuery);

    List<Order> findBySchedule(Schedule schedule);

    List<Order> listOrderByClient(Client client);

//    List<Order> findAll();

}
