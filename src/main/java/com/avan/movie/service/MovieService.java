package com.avan.movie.service;

import com.avan.movie.po.Movie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface MovieService {

    Movie saveMovie(Movie movie);

    Page<Movie> listMovie(Pageable pageable);

    List<Movie> listMovie();

    List<Movie> listOnlneMovie();

    List<Movie> listOnshowingMovie();

    Page<Movie> listConditionMovie(Pageable pageable, Movie movie);

    Movie updateMovie(Long id, Movie movie);

    Movie getMovie(Long id);

    void deleteMovie(Long id);
}
