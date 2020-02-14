package com.avan.movie.dao;

import com.avan.movie.po.Client;
import com.avan.movie.po.Order;
import com.avan.movie.po.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long>, JpaSpecificationExecutor<Order> {

    @Query("select o from Order o where o.schedule = ?1 ")
    List<Order> findBySchedule(Schedule schedule);

    @Query("select o from Order o where o.client = ?1 order by o.createTime desc ")
    List<Order> findByClient(Client client);

}
