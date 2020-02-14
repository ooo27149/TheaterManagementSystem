package com.avan.movie.dao;

import com.avan.movie.po.Tweet;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TweetRepository extends JpaRepository<Tweet, Long>, JpaSpecificationExecutor<Tweet> {

    @Query("select s from Tweet s where s.published = true ")
    Page<Tweet> findPublishedTweet(Pageable pageable);


}
