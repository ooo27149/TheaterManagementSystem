package com.avan.movie.service;

import com.avan.movie.po.Tweet;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TweetService {

    Tweet getTweet(Long id);

    Page<Tweet> listTweet(Pageable pageable, Tweet tweet);

    Page<Tweet> listTweet(Pageable pageable);

    Tweet savaTweet(Tweet tweet);

    Tweet updateTweet(Long id, Tweet tweet);

    void deleteTweet(Long id);

    Tweet getAndConvertTweet(Long id);
}
