package com.avan.movie.service;

import com.avan.movie.NotFoundException;
import com.avan.movie.dao.AdminRepository;
import com.avan.movie.po.Admin;
import com.avan.movie.po.Movie;
import com.avan.movie.util.MD5Utils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.Transient;
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
import java.util.Date;
import java.util.List;


@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private AdminRepository adminRepository;

    @Transactional
    @Override
    public Admin checkAdmin(String phone, String password) {
        Admin admin = adminRepository.findByPhoneAndPassword(phone, password);
        return admin;
    }

    @Transactional
    @Override
    public Admin saveAdmin(Admin admin) {
        return adminRepository.save(admin);
    }

    @Transactional
    @Override
    public Admin getAdmin(Long id) {
        return adminRepository.findOne(id);
    }

    @Transactional
    @Override
    public Admin getAdminByPhone(String phone) {
        return adminRepository.findByPhone(phone);
    }

    @Transactional
    @Override
    public Page<Admin> listAdmin(Pageable pageable) {
        return adminRepository.findAll(pageable);
    }

    @Transactional
    @Override
    public Page<Admin> listAdmin(Pageable pageable, Admin admin) {

        return adminRepository.findAll(new Specification<Admin>() {
            @Override
            public Predicate toPredicate(Root<Admin> root, CriteriaQuery<?> cQ, CriteriaBuilder cB) {
                List<Predicate> predicates = new ArrayList<>();
                if (!"".equals(admin.getNickname()) && admin.getNickname() != null) {
                    predicates.add(cB.like(root.<String>get("nickname"), "%" + admin.getNickname() + "%"));
                }
                if (!"".equals(admin.getLevel()) && admin.getLevel() != null) {
                    predicates.add(cB.like(root.<String>get("level"), "%" + admin.getLevel() + "%"));
                }
                cQ.where(predicates.toArray(new Predicate[predicates.size()]));
                return null;
            }
        }, pageable);
    }

    @Transactional
    @Override
    public Admin updateAdmin(Long id, Admin admin) {
        Admin temp = adminRepository.findOne(id);
        if (temp == null) {
            throw new NotFoundException("the admin does not exist");
        }
        BeanUtils.copyProperties(admin, temp);
        temp.setUpdateTime(new Date());
        return adminRepository.save(temp);
    }

    @Transactional
    @Override
    public void deleteAdmin(Long id) {
        adminRepository.delete(id);
    }
}
