package com.avan.movie.service;

import com.avan.movie.NotFoundException;
import com.avan.movie.dao.ScreenRepository;
import com.avan.movie.po.Screen;
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
public class ScreenServiceImpl implements ScreenService {

    @Autowired
    private ScreenRepository screenRepository;

    @Transactional
    @Override
    public Screen saveScreen(Screen screen) {
        return screenRepository.save(screen);
    }

    @Transactional
    @Override
    public Screen getScreen(Long id) {
        return screenRepository.findOne(id);
    }

    @Transactional
    @Override
    public Page<Screen> listScreen(Pageable pageable, Screen screen) {

        return screenRepository.findAll(new Specification<Screen>() {
            @Override
            public Predicate toPredicate(Root<Screen> root, CriteriaQuery<?> cQ, CriteriaBuilder cB) {
                List<Predicate> predicates = new ArrayList<>();
                if (!"".equals(screen.getName()) && screen.getName() != null) {
                    predicates.add(cB.like(root.<String>get("name"), "%" + screen.getName() + "%"));
                }
                cQ.where(predicates.toArray(new Predicate[predicates.size()]));
                return null;
            }
        }, pageable);
    }

    @Override
    public List<Screen> listScreen() {
        return screenRepository.findAll();
    }

    @Transactional
    @Override
    public Screen updateScreen(Long id, Screen screen) {
        Screen temp = screenRepository.findOne(id);
        if (temp == null) {
            throw new NotFoundException("the screen doesn't exist");
        }
        BeanUtils.copyProperties(screen, temp);
        return screenRepository.save(temp);
    }

    @Transactional
    @Override
    public void deleteScreen(Long id) {
        screenRepository.delete(id);
    }

    @Transactional
    @Override
    public Screen getScreenByName(String name) {
        return screenRepository.findByName(name);
    }
}
