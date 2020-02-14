package com.avan.movie.dao;

import com.avan.movie.po.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ClientRepository extends JpaRepository<Client, Long>, JpaSpecificationExecutor<Client> {

    Client findByPhoneAndPassword(String phone, String password);

    Client findByPhone(String phone);
}
