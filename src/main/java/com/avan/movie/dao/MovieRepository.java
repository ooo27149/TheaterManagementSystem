package com.avan.movie.dao;

import com.avan.movie.po.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MovieRepository extends JpaRepository<Movie, Long>, JpaSpecificationExecutor<Movie> {

    Movie findById(Long id);

    @Query("select s from Movie s where s.stat = '正在热映' ")
    List<Movie> findOnlineMovie();

    @Query("select distinct m from Movie m, Schedule s where s.movie.id = m.id and s.stat = true order by m.id desc ")
    List<Movie> findShowingMovie();
}
