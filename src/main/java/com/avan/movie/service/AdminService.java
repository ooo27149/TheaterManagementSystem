package com.avan.movie.service;

import com.avan.movie.po.Admin;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AdminService {

    Admin checkAdmin(String phone, String password);

    Admin saveAdmin(Admin admin);

    Admin getAdmin(Long id);

    Admin getAdminByPhone(String phone);

    Page<Admin> listAdmin(Pageable pageable);

    Page<Admin> listAdmin(Pageable pageable, Admin admin);

    Admin updateAdmin(Long id, Admin admin);

    void deleteAdmin(Long id);
}
