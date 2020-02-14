package com.avan.movie.service;

import com.avan.movie.NotFoundException;
import com.avan.movie.dao.MovieRepository;
import com.avan.movie.po.Movie;
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
public class MovieServiceImpl implements MovieService {

    @Autowired
    private MovieRepository movieRepository;


    @Transactional
    @Override
    public Movie saveMovie(Movie movie) {
        return movieRepository.save(movie);
    }

    @Transactional
    @Override
    public Page<Movie> listMovie(Pageable pageable) {
        return movieRepository.findAll(pageable);
    }

    @Transactional
    @Override
    public List<Movie> listMovie() {
        return movieRepository.findAll();
    }

    @Override
    public List<Movie> listOnlneMovie() {
        return movieRepository.findOnlineMovie();
    }

    @Override
    public List<Movie> listOnshowingMovie() {
        return movieRepository.findShowingMovie();
    }

    @Transactional
    @Override
    public Page<Movie> listConditionMovie(Pageable pageable, Movie movie) {
        return movieRepository.findAll(new Specification<Movie>() {
            @Override
            public Predicate toPredicate(Root<Movie> root, CriteriaQuery<?> cQ, CriteriaBuilder cB) {
                List<Predicate> predicates = new ArrayList<>();
                if (!"".equals(movie.getName()) && movie.getName() != null) {
                    predicates.add(cB.like(root.<String>get("name"), "%"+movie.getName()+"%"));
                }
                if (!"".equals(movie.getGenre()) && movie.getGenre() != null) {
                    predicates.add(cB.like(root.<String>get("language"), "%"+movie.getLanguage()+"%"));
                }
                cQ.where(predicates.toArray(new Predicate[predicates.size()]));
                return null;
            }
        }, pageable);
    }

    @Transactional
    @Override
    public Movie updateMovie(Long id, Movie movie) {
        Movie temp = movieRepository.findOne(id);
        if (temp == null) {
            throw new NotFoundException("the movie does not exist");
        }
        BeanUtils.copyProperties(movie, temp);
        return movieRepository.save(temp);
    }

    @Transactional
    @Override
    public Movie getMovie(Long id) {
        return movieRepository.findOne(id);
    }

    @Transactional
    @Override
    public void deleteMovie(Long id) {
        movieRepository.delete(id);
    }
}
