package com.avan.movie.service;

import com.avan.movie.NotFoundException;
import com.avan.movie.dao.OrderRepository;
import com.avan.movie.po.*;
import com.avan.movie.vo.OrderQuery;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Transactional
    @Override
    public Order saveOrder(Order order) {
        return orderRepository.save(order);
    }

    @Transactional
    @Override
    public Order getOrder(Long id) {
        return orderRepository.findOne(id);
    }

    @Transactional
    @Override
    public Order updateOrder(Long id, Order order) {
        Order temp = orderRepository.findOne(id);
        if (temp == null) {
            throw new NotFoundException("the order doesn't exist");
        }
        BeanUtils.copyProperties(order, temp);
        return orderRepository.save(temp);
    }

    @Transactional
    @Override
    public void deleteOrder(Long id) {
        Order order = orderRepository.findOne(id);
        order.getSeats().removeAll(order.getSeats());
        orderRepository.delete(id);
    }

    @Transactional
    @Override
    public Page<Order> listOrder(Pageable pageable, OrderQuery orderQuery) {
        return orderRepository.findAll(new Specification<Order>() {
            @Override
            public Predicate toPredicate(Root<Order> root, CriteriaQuery<?> cQ, CriteriaBuilder cB) {
                List<Predicate> predicates = new ArrayList<>();
                if (!"".equals(orderQuery.getMovieName()) && orderQuery.getMovieName() != null) {
                    predicates.add(cB.like(root.<Schedule>get("schedule").<Movie>get("movie").<String>get("name"), "%" + orderQuery.getMovieName() + "%"));
                }
                if (!"".equals(orderQuery.getPhone()) && orderQuery.getPhone() != null) {
                    predicates.add(cB.like(root.<Client>get("client").get("phone"), "%" +orderQuery.getPhone()+ "%"));
//                    predicates.add(cB.like(root.<Admin>get("admin").get("phone"), "%" +orderQuery.getPhone()+ "%"));
                }
                cQ.where(predicates.toArray(new Predicate[predicates.size()]));
                return null;
            }
        }, pageable);
    }

    @Override
    public List<Order> findBySchedule(Schedule schedule) {
        return orderRepository.findBySchedule(schedule);
    }

    @Override
    public List<Order> listOrderByClient(Client client) {
        return orderRepository.findByClient(client);
    }
}
