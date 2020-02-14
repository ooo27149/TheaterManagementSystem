package com.avan.movie.service;

import com.avan.movie.NotFoundException;
import com.avan.movie.dao.ScheduleRepository;
import com.avan.movie.po.Movie;
import com.avan.movie.po.Schedule;
import com.avan.movie.po.Screen;
import com.avan.movie.vo.ScheduleQuery;
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
public class ScheduleServiceImpl implements ScheduleService {

    @Autowired
    private ScheduleRepository scheduleRepository;

    @Transactional
    @Override
    public Schedule saveSchedule(Schedule schedule) {
        return scheduleRepository.save(schedule);
    }

    @Transactional
    @Override
    public Schedule getSchedule(Long id) {
        return scheduleRepository.findOne(id);
    }

    @Transactional
    @Override
    public Page<Schedule> listSchedule(Pageable pageable, ScheduleQuery scheduleQuery) {

        return scheduleRepository.findAll(new Specification<Schedule>() {
            @Override
            public Predicate toPredicate(Root<Schedule> root, CriteriaQuery<?> cQ, CriteriaBuilder cB) {
                List<Predicate> predicates = new ArrayList<>();
                if (!"".equals(scheduleQuery.getMovieName()) && scheduleQuery.getMovieName() != null) {
                    predicates.add(cB.like(root.<Movie>get("movie").<String>get("name"), "%" + scheduleQuery.getMovieName() + "%"));
                }
                if (scheduleQuery.getScreenId() != null) {
                    predicates.add(cB.equal(root.<Screen>get("screen").get("id"), scheduleQuery.getScreenId()));
                }
                cQ.where(predicates.toArray(new Predicate[predicates.size()]));
                return null;
            }
        }, pageable);
    }

    @Transactional
    @Override
    public Schedule updateSchedule(Long id, Schedule schedule) {
        Schedule s = scheduleRepository.findOne(id);
        if (s == null) {
            throw new NotFoundException("the schedule doesn't exist");
        }
        BeanUtils.copyProperties(schedule, s);
        return scheduleRepository.save(s);
    }

    @Transactional
    @Override
    public void deleteSchedule(Long id) {
        scheduleRepository.delete(id);
    }

    @Override
    public List<Schedule> getOnlineSchedule() {
        return scheduleRepository.onlineSchedule();
    }

    @Override
    public Page<Schedule> listOnlineSchedule(Pageable pageable, ScheduleQuery scheduleQuery) {
        return scheduleRepository.findAll(new Specification<Schedule>() {
            @Override
            public Predicate toPredicate(Root<Schedule> root, CriteriaQuery<?> cQ, CriteriaBuilder cB) {
                Boolean b = true;
                List<Predicate> predicates = new ArrayList<>();
                if (!"".equals(scheduleQuery.getMovieName()) && scheduleQuery.getMovieName() != null) {
                    predicates.add(cB.like(root.<Movie>get("movie").<String>get("name"), "%" + scheduleQuery.getMovieName() + "%"));
                }
                if (scheduleQuery.getScreenId() != null) {
                    predicates.add(cB.equal(root.<Screen>get("screen").get("id"), scheduleQuery.getScreenId()));
                }
                predicates.add(cB.equal(root.<Boolean>get("stat"), b));
                cQ.where(predicates.toArray(new Predicate[predicates.size()]));
                return null;
            }
        }, pageable);
    }

    @Override
    public List<Schedule> listOnlineScheduleByScreen(Screen screen) {
        return scheduleRepository.findOnlineScheduleByScreen(screen);
    }

    @Override
    public List<Schedule> listScheduleByMovie(Movie movie) {
        return scheduleRepository.findScheduleByMovie(movie);
    }
}
