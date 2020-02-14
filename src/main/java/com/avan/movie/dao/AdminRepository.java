package com.avan.movie.dao;

import com.avan.movie.po.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface AdminRepository extends JpaRepository<Admin, Long>, JpaSpecificationExecutor<Admin> {

    Admin findByPhoneAndPassword(String phone, String password);

    Admin findByPhone(String phone);
}
